package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.IscilikTipi;
import tr.com.mavi.oto.repository.IscilikTipiRepository;
import tr.com.mavi.oto.repository.search.IscilikTipiSearchRepository;
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
 * Integration tests for the {@Link IscilikTipiResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class IscilikTipiResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final Double DEFAULT_VARSAYILAN_FIYAT = 1D;
    private static final Double UPDATED_VARSAYILAN_FIYAT = 2D;

    @Autowired
    private IscilikTipiRepository iscilikTipiRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.IscilikTipiSearchRepositoryMockConfiguration
     */
    @Autowired
    private IscilikTipiSearchRepository mockIscilikTipiSearchRepository;

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

    private MockMvc restIscilikTipiMockMvc;

    private IscilikTipi iscilikTipi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IscilikTipiResource iscilikTipiResource = new IscilikTipiResource(iscilikTipiRepository, mockIscilikTipiSearchRepository);
        this.restIscilikTipiMockMvc = MockMvcBuilders.standaloneSetup(iscilikTipiResource)
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
    public static IscilikTipi createEntity(EntityManager em) {
        IscilikTipi iscilikTipi = new IscilikTipi()
            .ad(DEFAULT_AD)
            .varsayilanFiyat(DEFAULT_VARSAYILAN_FIYAT);
        return iscilikTipi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IscilikTipi createUpdatedEntity(EntityManager em) {
        IscilikTipi iscilikTipi = new IscilikTipi()
            .ad(UPDATED_AD)
            .varsayilanFiyat(UPDATED_VARSAYILAN_FIYAT);
        return iscilikTipi;
    }

    @BeforeEach
    public void initTest() {
        iscilikTipi = createEntity(em);
    }

    @Test
    @Transactional
    public void createIscilikTipi() throws Exception {
        int databaseSizeBeforeCreate = iscilikTipiRepository.findAll().size();

        // Create the IscilikTipi
        restIscilikTipiMockMvc.perform(post("/api/iscilik-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilikTipi)))
            .andExpect(status().isCreated());

        // Validate the IscilikTipi in the database
        List<IscilikTipi> iscilikTipiList = iscilikTipiRepository.findAll();
        assertThat(iscilikTipiList).hasSize(databaseSizeBeforeCreate + 1);
        IscilikTipi testIscilikTipi = iscilikTipiList.get(iscilikTipiList.size() - 1);
        assertThat(testIscilikTipi.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testIscilikTipi.getVarsayilanFiyat()).isEqualTo(DEFAULT_VARSAYILAN_FIYAT);

        // Validate the IscilikTipi in Elasticsearch
        verify(mockIscilikTipiSearchRepository, times(1)).save(testIscilikTipi);
    }

    @Test
    @Transactional
    public void createIscilikTipiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = iscilikTipiRepository.findAll().size();

        // Create the IscilikTipi with an existing ID
        iscilikTipi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIscilikTipiMockMvc.perform(post("/api/iscilik-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilikTipi)))
            .andExpect(status().isBadRequest());

        // Validate the IscilikTipi in the database
        List<IscilikTipi> iscilikTipiList = iscilikTipiRepository.findAll();
        assertThat(iscilikTipiList).hasSize(databaseSizeBeforeCreate);

        // Validate the IscilikTipi in Elasticsearch
        verify(mockIscilikTipiSearchRepository, times(0)).save(iscilikTipi);
    }


    @Test
    @Transactional
    public void getAllIscilikTipis() throws Exception {
        // Initialize the database
        iscilikTipiRepository.saveAndFlush(iscilikTipi);

        // Get all the iscilikTipiList
        restIscilikTipiMockMvc.perform(get("/api/iscilik-tipis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iscilikTipi.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())))
            .andExpect(jsonPath("$.[*].varsayilanFiyat").value(hasItem(DEFAULT_VARSAYILAN_FIYAT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getIscilikTipi() throws Exception {
        // Initialize the database
        iscilikTipiRepository.saveAndFlush(iscilikTipi);

        // Get the iscilikTipi
        restIscilikTipiMockMvc.perform(get("/api/iscilik-tipis/{id}", iscilikTipi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(iscilikTipi.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()))
            .andExpect(jsonPath("$.varsayilanFiyat").value(DEFAULT_VARSAYILAN_FIYAT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIscilikTipi() throws Exception {
        // Get the iscilikTipi
        restIscilikTipiMockMvc.perform(get("/api/iscilik-tipis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIscilikTipi() throws Exception {
        // Initialize the database
        iscilikTipiRepository.saveAndFlush(iscilikTipi);

        int databaseSizeBeforeUpdate = iscilikTipiRepository.findAll().size();

        // Update the iscilikTipi
        IscilikTipi updatedIscilikTipi = iscilikTipiRepository.findById(iscilikTipi.getId()).get();
        // Disconnect from session so that the updates on updatedIscilikTipi are not directly saved in db
        em.detach(updatedIscilikTipi);
        updatedIscilikTipi
            .ad(UPDATED_AD)
            .varsayilanFiyat(UPDATED_VARSAYILAN_FIYAT);

        restIscilikTipiMockMvc.perform(put("/api/iscilik-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIscilikTipi)))
            .andExpect(status().isOk());

        // Validate the IscilikTipi in the database
        List<IscilikTipi> iscilikTipiList = iscilikTipiRepository.findAll();
        assertThat(iscilikTipiList).hasSize(databaseSizeBeforeUpdate);
        IscilikTipi testIscilikTipi = iscilikTipiList.get(iscilikTipiList.size() - 1);
        assertThat(testIscilikTipi.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testIscilikTipi.getVarsayilanFiyat()).isEqualTo(UPDATED_VARSAYILAN_FIYAT);

        // Validate the IscilikTipi in Elasticsearch
        verify(mockIscilikTipiSearchRepository, times(1)).save(testIscilikTipi);
    }

    @Test
    @Transactional
    public void updateNonExistingIscilikTipi() throws Exception {
        int databaseSizeBeforeUpdate = iscilikTipiRepository.findAll().size();

        // Create the IscilikTipi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIscilikTipiMockMvc.perform(put("/api/iscilik-tipis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilikTipi)))
            .andExpect(status().isBadRequest());

        // Validate the IscilikTipi in the database
        List<IscilikTipi> iscilikTipiList = iscilikTipiRepository.findAll();
        assertThat(iscilikTipiList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IscilikTipi in Elasticsearch
        verify(mockIscilikTipiSearchRepository, times(0)).save(iscilikTipi);
    }

    @Test
    @Transactional
    public void deleteIscilikTipi() throws Exception {
        // Initialize the database
        iscilikTipiRepository.saveAndFlush(iscilikTipi);

        int databaseSizeBeforeDelete = iscilikTipiRepository.findAll().size();

        // Delete the iscilikTipi
        restIscilikTipiMockMvc.perform(delete("/api/iscilik-tipis/{id}", iscilikTipi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<IscilikTipi> iscilikTipiList = iscilikTipiRepository.findAll();
        assertThat(iscilikTipiList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IscilikTipi in Elasticsearch
        verify(mockIscilikTipiSearchRepository, times(1)).deleteById(iscilikTipi.getId());
    }

    @Test
    @Transactional
    public void searchIscilikTipi() throws Exception {
        // Initialize the database
        iscilikTipiRepository.saveAndFlush(iscilikTipi);
        when(mockIscilikTipiSearchRepository.search(queryStringQuery("id:" + iscilikTipi.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(iscilikTipi), PageRequest.of(0, 1), 1));
        // Search the iscilikTipi
        restIscilikTipiMockMvc.perform(get("/api/_search/iscilik-tipis?query=id:" + iscilikTipi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iscilikTipi.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].varsayilanFiyat").value(hasItem(DEFAULT_VARSAYILAN_FIYAT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IscilikTipi.class);
        IscilikTipi iscilikTipi1 = new IscilikTipi();
        iscilikTipi1.setId(1L);
        IscilikTipi iscilikTipi2 = new IscilikTipi();
        iscilikTipi2.setId(iscilikTipi1.getId());
        assertThat(iscilikTipi1).isEqualTo(iscilikTipi2);
        iscilikTipi2.setId(2L);
        assertThat(iscilikTipi1).isNotEqualTo(iscilikTipi2);
        iscilikTipi1.setId(null);
        assertThat(iscilikTipi1).isNotEqualTo(iscilikTipi2);
    }
}
