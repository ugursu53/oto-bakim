package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.ParcaTipi;
import tr.com.mavi.oto.repository.ParcaTipiRepository;
import tr.com.mavi.oto.repository.search.ParcaTipiSearchRepository;
import tr.com.mavi.oto.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static tr.com.mavi.oto.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ParcaTipiResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class ParcaTipiResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final String DEFAULT_KODU = "AAAAAAAAAA";
    private static final String UPDATED_KODU = "BBBBBBBBBB";

    private static final Double DEFAULT_VARSAYILAN_FIYATI = 1D;
    private static final Double UPDATED_VARSAYILAN_FIYATI = 2D;

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    @Autowired
    private ParcaTipiRepository parcaTipiRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.ParcaTipiSearchRepositoryMockConfiguration
     */
    @Autowired
    private ParcaTipiSearchRepository mockParcaTipiSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restParcaTipiMockMvc;

    private ParcaTipi parcaTipi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParcaTipiResource parcaTipiResource = new ParcaTipiResource(parcaTipiRepository, mockParcaTipiSearchRepository);
        this.restParcaTipiMockMvc = MockMvcBuilders.standaloneSetup(parcaTipiResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParcaTipi createEntity(EntityManager em) {
        ParcaTipi parcaTipi = new ParcaTipi()
            .ad(DEFAULT_AD)
            .kodu(DEFAULT_KODU)
            .varsayilanFiyati(DEFAULT_VARSAYILAN_FIYATI)
            .aciklama(DEFAULT_ACIKLAMA);
        return parcaTipi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParcaTipi createUpdatedEntity(EntityManager em) {
        ParcaTipi parcaTipi = new ParcaTipi()
            .ad(UPDATED_AD)
            .kodu(UPDATED_KODU)
            .varsayilanFiyati(UPDATED_VARSAYILAN_FIYATI)
            .aciklama(UPDATED_ACIKLAMA);
        return parcaTipi;
    }

    @BeforeEach
    public void initTest() {
        parcaTipi = createEntity(em);
    }

    @Test
    @Transactional
    public void createParcaTipi() throws Exception {
        int databaseSizeBeforeCreate = parcaTipiRepository.findAll().size();

        // Create the ParcaTipi
        restParcaTipiMockMvc.perform(post("/api/parca-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcaTipi)))
            .andExpect(status().isCreated());

        // Validate the ParcaTipi in the database
        List<ParcaTipi> parcaTipiList = parcaTipiRepository.findAll();
        assertThat(parcaTipiList).hasSize(databaseSizeBeforeCreate + 1);
        ParcaTipi testParcaTipi = parcaTipiList.get(parcaTipiList.size() - 1);
        assertThat(testParcaTipi.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testParcaTipi.getKodu()).isEqualTo(DEFAULT_KODU);
        assertThat(testParcaTipi.getVarsayilanFiyati()).isEqualTo(DEFAULT_VARSAYILAN_FIYATI);
        assertThat(testParcaTipi.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);

        // Validate the ParcaTipi in Elasticsearch
        verify(mockParcaTipiSearchRepository, times(1)).save(testParcaTipi);
    }

    @Test
    @Transactional
    public void createParcaTipiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parcaTipiRepository.findAll().size();

        // Create the ParcaTipi with an existing ID
        parcaTipi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParcaTipiMockMvc.perform(post("/api/parca-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcaTipi)))
            .andExpect(status().isBadRequest());

        // Validate the ParcaTipi in the database
        List<ParcaTipi> parcaTipiList = parcaTipiRepository.findAll();
        assertThat(parcaTipiList).hasSize(databaseSizeBeforeCreate);

        // Validate the ParcaTipi in Elasticsearch
        verify(mockParcaTipiSearchRepository, times(0)).save(parcaTipi);
    }


    @Test
    @Transactional
    public void checkAdIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcaTipiRepository.findAll().size();
        // set the field null
        parcaTipi.setAd(null);

        // Create the ParcaTipi, which fails.

        restParcaTipiMockMvc.perform(post("/api/parca-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcaTipi)))
            .andExpect(status().isBadRequest());

        List<ParcaTipi> parcaTipiList = parcaTipiRepository.findAll();
        assertThat(parcaTipiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParcaTipis() throws Exception {
        // Initialize the database
        parcaTipiRepository.saveAndFlush(parcaTipi);

        // Get all the parcaTipiList
        restParcaTipiMockMvc.perform(get("/api/parca-tipis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parcaTipi.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())))
            .andExpect(jsonPath("$.[*].kodu").value(hasItem(DEFAULT_KODU.toString())))
            .andExpect(jsonPath("$.[*].varsayilanFiyati").value(hasItem(DEFAULT_VARSAYILAN_FIYATI.doubleValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())));
    }
    
    @Test
    @Transactional
    public void getParcaTipi() throws Exception {
        // Initialize the database
        parcaTipiRepository.saveAndFlush(parcaTipi);

        // Get the parcaTipi
        restParcaTipiMockMvc.perform(get("/api/parca-tipis/{id}", parcaTipi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parcaTipi.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()))
            .andExpect(jsonPath("$.kodu").value(DEFAULT_KODU.toString()))
            .andExpect(jsonPath("$.varsayilanFiyati").value(DEFAULT_VARSAYILAN_FIYATI.doubleValue()))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParcaTipi() throws Exception {
        // Get the parcaTipi
        restParcaTipiMockMvc.perform(get("/api/parca-tipis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParcaTipi() throws Exception {
        // Initialize the database
        parcaTipiRepository.saveAndFlush(parcaTipi);

        int databaseSizeBeforeUpdate = parcaTipiRepository.findAll().size();

        // Update the parcaTipi
        ParcaTipi updatedParcaTipi = parcaTipiRepository.findById(parcaTipi.getId()).get();
        // Disconnect from session so that the updates on updatedParcaTipi are not directly saved in db
        em.detach(updatedParcaTipi);
        updatedParcaTipi
            .ad(UPDATED_AD)
            .kodu(UPDATED_KODU)
            .varsayilanFiyati(UPDATED_VARSAYILAN_FIYATI)
            .aciklama(UPDATED_ACIKLAMA);

        restParcaTipiMockMvc.perform(put("/api/parca-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParcaTipi)))
            .andExpect(status().isOk());

        // Validate the ParcaTipi in the database
        List<ParcaTipi> parcaTipiList = parcaTipiRepository.findAll();
        assertThat(parcaTipiList).hasSize(databaseSizeBeforeUpdate);
        ParcaTipi testParcaTipi = parcaTipiList.get(parcaTipiList.size() - 1);
        assertThat(testParcaTipi.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testParcaTipi.getKodu()).isEqualTo(UPDATED_KODU);
        assertThat(testParcaTipi.getVarsayilanFiyati()).isEqualTo(UPDATED_VARSAYILAN_FIYATI);
        assertThat(testParcaTipi.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);

        // Validate the ParcaTipi in Elasticsearch
        verify(mockParcaTipiSearchRepository, times(1)).save(testParcaTipi);
    }

    @Test
    @Transactional
    public void updateNonExistingParcaTipi() throws Exception {
        int databaseSizeBeforeUpdate = parcaTipiRepository.findAll().size();

        // Create the ParcaTipi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcaTipiMockMvc.perform(put("/api/parca-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcaTipi)))
            .andExpect(status().isBadRequest());

        // Validate the ParcaTipi in the database
        List<ParcaTipi> parcaTipiList = parcaTipiRepository.findAll();
        assertThat(parcaTipiList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParcaTipi in Elasticsearch
        verify(mockParcaTipiSearchRepository, times(0)).save(parcaTipi);
    }

    @Test
    @Transactional
    public void deleteParcaTipi() throws Exception {
        // Initialize the database
        parcaTipiRepository.saveAndFlush(parcaTipi);

        int databaseSizeBeforeDelete = parcaTipiRepository.findAll().size();

        // Delete the parcaTipi
        restParcaTipiMockMvc.perform(delete("/api/parca-tipis/{id}", parcaTipi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<ParcaTipi> parcaTipiList = parcaTipiRepository.findAll();
        assertThat(parcaTipiList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ParcaTipi in Elasticsearch
        verify(mockParcaTipiSearchRepository, times(1)).deleteById(parcaTipi.getId());
    }

    @Test
    @Transactional
    public void searchParcaTipi() throws Exception {
        // Initialize the database
        parcaTipiRepository.saveAndFlush(parcaTipi);
        when(mockParcaTipiSearchRepository.search(queryStringQuery("id:" + parcaTipi.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(parcaTipi), PageRequest.of(0, 1), 1));
        // Search the parcaTipi
        restParcaTipiMockMvc.perform(get("/api/_search/parca-tipis?query=id:" + parcaTipi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parcaTipi.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].kodu").value(hasItem(DEFAULT_KODU)))
            .andExpect(jsonPath("$.[*].varsayilanFiyati").value(hasItem(DEFAULT_VARSAYILAN_FIYATI.doubleValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParcaTipi.class);
        ParcaTipi parcaTipi1 = new ParcaTipi();
        parcaTipi1.setId(1L);
        ParcaTipi parcaTipi2 = new ParcaTipi();
        parcaTipi2.setId(parcaTipi1.getId());
        assertThat(parcaTipi1).isEqualTo(parcaTipi2);
        parcaTipi2.setId(2L);
        assertThat(parcaTipi1).isNotEqualTo(parcaTipi2);
        parcaTipi1.setId(null);
        assertThat(parcaTipi1).isNotEqualTo(parcaTipi2);
    }
}
