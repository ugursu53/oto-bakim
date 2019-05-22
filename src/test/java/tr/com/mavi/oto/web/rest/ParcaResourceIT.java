package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Parca;
import tr.com.mavi.oto.repository.ParcaRepository;
import tr.com.mavi.oto.repository.search.ParcaSearchRepository;
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
 * Integration tests for the {@Link ParcaResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class ParcaResourceIT {

    private static final Double DEFAULT_FIYATI = 1D;
    private static final Double UPDATED_FIYATI = 2D;

    @Autowired
    private ParcaRepository parcaRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.ParcaSearchRepositoryMockConfiguration
     */
    @Autowired
    private ParcaSearchRepository mockParcaSearchRepository;

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

    private MockMvc restParcaMockMvc;

    private Parca parca;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParcaResource parcaResource = new ParcaResource(parcaRepository, mockParcaSearchRepository);
        this.restParcaMockMvc = MockMvcBuilders.standaloneSetup(parcaResource)
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
    public static Parca createEntity(EntityManager em) {
        Parca parca = new Parca()
            .fiyati(DEFAULT_FIYATI);
        return parca;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parca createUpdatedEntity(EntityManager em) {
        Parca parca = new Parca()
            .fiyati(UPDATED_FIYATI);
        return parca;
    }

    @BeforeEach
    public void initTest() {
        parca = createEntity(em);
    }

    @Test
    @Transactional
    public void createParca() throws Exception {
        int databaseSizeBeforeCreate = parcaRepository.findAll().size();

        // Create the Parca
        restParcaMockMvc.perform(post("/api/parcas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parca)))
            .andExpect(status().isCreated());

        // Validate the Parca in the database
        List<Parca> parcaList = parcaRepository.findAll();
        assertThat(parcaList).hasSize(databaseSizeBeforeCreate + 1);
        Parca testParca = parcaList.get(parcaList.size() - 1);
        assertThat(testParca.getFiyati()).isEqualTo(DEFAULT_FIYATI);

        // Validate the Parca in Elasticsearch
        verify(mockParcaSearchRepository, times(1)).save(testParca);
    }

    @Test
    @Transactional
    public void createParcaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parcaRepository.findAll().size();

        // Create the Parca with an existing ID
        parca.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParcaMockMvc.perform(post("/api/parcas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parca)))
            .andExpect(status().isBadRequest());

        // Validate the Parca in the database
        List<Parca> parcaList = parcaRepository.findAll();
        assertThat(parcaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Parca in Elasticsearch
        verify(mockParcaSearchRepository, times(0)).save(parca);
    }


    @Test
    @Transactional
    public void getAllParcas() throws Exception {
        // Initialize the database
        parcaRepository.saveAndFlush(parca);

        // Get all the parcaList
        restParcaMockMvc.perform(get("/api/parcas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parca.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiyati").value(hasItem(DEFAULT_FIYATI.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getParca() throws Exception {
        // Initialize the database
        parcaRepository.saveAndFlush(parca);

        // Get the parca
        restParcaMockMvc.perform(get("/api/parcas/{id}", parca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parca.getId().intValue()))
            .andExpect(jsonPath("$.fiyati").value(DEFAULT_FIYATI.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParca() throws Exception {
        // Get the parca
        restParcaMockMvc.perform(get("/api/parcas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParca() throws Exception {
        // Initialize the database
        parcaRepository.saveAndFlush(parca);

        int databaseSizeBeforeUpdate = parcaRepository.findAll().size();

        // Update the parca
        Parca updatedParca = parcaRepository.findById(parca.getId()).get();
        // Disconnect from session so that the updates on updatedParca are not directly saved in db
        em.detach(updatedParca);
        updatedParca
            .fiyati(UPDATED_FIYATI);

        restParcaMockMvc.perform(put("/api/parcas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParca)))
            .andExpect(status().isOk());

        // Validate the Parca in the database
        List<Parca> parcaList = parcaRepository.findAll();
        assertThat(parcaList).hasSize(databaseSizeBeforeUpdate);
        Parca testParca = parcaList.get(parcaList.size() - 1);
        assertThat(testParca.getFiyati()).isEqualTo(UPDATED_FIYATI);

        // Validate the Parca in Elasticsearch
        verify(mockParcaSearchRepository, times(1)).save(testParca);
    }

    @Test
    @Transactional
    public void updateNonExistingParca() throws Exception {
        int databaseSizeBeforeUpdate = parcaRepository.findAll().size();

        // Create the Parca

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcaMockMvc.perform(put("/api/parcas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parca)))
            .andExpect(status().isBadRequest());

        // Validate the Parca in the database
        List<Parca> parcaList = parcaRepository.findAll();
        assertThat(parcaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Parca in Elasticsearch
        verify(mockParcaSearchRepository, times(0)).save(parca);
    }

    @Test
    @Transactional
    public void deleteParca() throws Exception {
        // Initialize the database
        parcaRepository.saveAndFlush(parca);

        int databaseSizeBeforeDelete = parcaRepository.findAll().size();

        // Delete the parca
        restParcaMockMvc.perform(delete("/api/parcas/{id}", parca.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Parca> parcaList = parcaRepository.findAll();
        assertThat(parcaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Parca in Elasticsearch
        verify(mockParcaSearchRepository, times(1)).deleteById(parca.getId());
    }

    @Test
    @Transactional
    public void searchParca() throws Exception {
        // Initialize the database
        parcaRepository.saveAndFlush(parca);
        when(mockParcaSearchRepository.search(queryStringQuery("id:" + parca.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(parca), PageRequest.of(0, 1), 1));
        // Search the parca
        restParcaMockMvc.perform(get("/api/_search/parcas?query=id:" + parca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parca.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiyati").value(hasItem(DEFAULT_FIYATI.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parca.class);
        Parca parca1 = new Parca();
        parca1.setId(1L);
        Parca parca2 = new Parca();
        parca2.setId(parca1.getId());
        assertThat(parca1).isEqualTo(parca2);
        parca2.setId(2L);
        assertThat(parca1).isNotEqualTo(parca2);
        parca1.setId(null);
        assertThat(parca1).isNotEqualTo(parca2);
    }
}
