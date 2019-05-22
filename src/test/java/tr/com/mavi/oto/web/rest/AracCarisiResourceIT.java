package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.AracCarisi;
import tr.com.mavi.oto.repository.AracCarisiRepository;
import tr.com.mavi.oto.repository.search.AracCarisiSearchRepository;
import tr.com.mavi.oto.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Integration tests for the {@Link AracCarisiResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class AracCarisiResourceIT {

    private static final Boolean DEFAULT_AKTIF = false;
    private static final Boolean UPDATED_AKTIF = true;

    @Autowired
    private AracCarisiRepository aracCarisiRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.AracCarisiSearchRepositoryMockConfiguration
     */
    @Autowired
    private AracCarisiSearchRepository mockAracCarisiSearchRepository;

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

    private MockMvc restAracCarisiMockMvc;

    private AracCarisi aracCarisi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AracCarisiResource aracCarisiResource = new AracCarisiResource(aracCarisiRepository, mockAracCarisiSearchRepository);
        this.restAracCarisiMockMvc = MockMvcBuilders.standaloneSetup(aracCarisiResource)
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
    public static AracCarisi createEntity(EntityManager em) {
        AracCarisi aracCarisi = new AracCarisi()
            .aktif(DEFAULT_AKTIF);
        return aracCarisi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AracCarisi createUpdatedEntity(EntityManager em) {
        AracCarisi aracCarisi = new AracCarisi()
            .aktif(UPDATED_AKTIF);
        return aracCarisi;
    }

    @BeforeEach
    public void initTest() {
        aracCarisi = createEntity(em);
    }

    @Test
    @Transactional
    public void createAracCarisi() throws Exception {
        int databaseSizeBeforeCreate = aracCarisiRepository.findAll().size();

        // Create the AracCarisi
        restAracCarisiMockMvc.perform(post("/api/arac-carisis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aracCarisi)))
            .andExpect(status().isCreated());

        // Validate the AracCarisi in the database
        List<AracCarisi> aracCarisiList = aracCarisiRepository.findAll();
        assertThat(aracCarisiList).hasSize(databaseSizeBeforeCreate + 1);
        AracCarisi testAracCarisi = aracCarisiList.get(aracCarisiList.size() - 1);
        assertThat(testAracCarisi.isAktif()).isEqualTo(DEFAULT_AKTIF);

        // Validate the AracCarisi in Elasticsearch
        verify(mockAracCarisiSearchRepository, times(1)).save(testAracCarisi);
    }

    @Test
    @Transactional
    public void createAracCarisiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aracCarisiRepository.findAll().size();

        // Create the AracCarisi with an existing ID
        aracCarisi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAracCarisiMockMvc.perform(post("/api/arac-carisis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aracCarisi)))
            .andExpect(status().isBadRequest());

        // Validate the AracCarisi in the database
        List<AracCarisi> aracCarisiList = aracCarisiRepository.findAll();
        assertThat(aracCarisiList).hasSize(databaseSizeBeforeCreate);

        // Validate the AracCarisi in Elasticsearch
        verify(mockAracCarisiSearchRepository, times(0)).save(aracCarisi);
    }


    @Test
    @Transactional
    public void checkAktifIsRequired() throws Exception {
        int databaseSizeBeforeTest = aracCarisiRepository.findAll().size();
        // set the field null
        aracCarisi.setAktif(null);

        // Create the AracCarisi, which fails.

        restAracCarisiMockMvc.perform(post("/api/arac-carisis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aracCarisi)))
            .andExpect(status().isBadRequest());

        List<AracCarisi> aracCarisiList = aracCarisiRepository.findAll();
        assertThat(aracCarisiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAracCarisis() throws Exception {
        // Initialize the database
        aracCarisiRepository.saveAndFlush(aracCarisi);

        // Get all the aracCarisiList
        restAracCarisiMockMvc.perform(get("/api/arac-carisis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aracCarisi.getId().intValue())))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAracCarisi() throws Exception {
        // Initialize the database
        aracCarisiRepository.saveAndFlush(aracCarisi);

        // Get the aracCarisi
        restAracCarisiMockMvc.perform(get("/api/arac-carisis/{id}", aracCarisi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aracCarisi.getId().intValue()))
            .andExpect(jsonPath("$.aktif").value(DEFAULT_AKTIF.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAracCarisi() throws Exception {
        // Get the aracCarisi
        restAracCarisiMockMvc.perform(get("/api/arac-carisis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAracCarisi() throws Exception {
        // Initialize the database
        aracCarisiRepository.saveAndFlush(aracCarisi);

        int databaseSizeBeforeUpdate = aracCarisiRepository.findAll().size();

        // Update the aracCarisi
        AracCarisi updatedAracCarisi = aracCarisiRepository.findById(aracCarisi.getId()).get();
        // Disconnect from session so that the updates on updatedAracCarisi are not directly saved in db
        em.detach(updatedAracCarisi);
        updatedAracCarisi
            .aktif(UPDATED_AKTIF);

        restAracCarisiMockMvc.perform(put("/api/arac-carisis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAracCarisi)))
            .andExpect(status().isOk());

        // Validate the AracCarisi in the database
        List<AracCarisi> aracCarisiList = aracCarisiRepository.findAll();
        assertThat(aracCarisiList).hasSize(databaseSizeBeforeUpdate);
        AracCarisi testAracCarisi = aracCarisiList.get(aracCarisiList.size() - 1);
        assertThat(testAracCarisi.isAktif()).isEqualTo(UPDATED_AKTIF);

        // Validate the AracCarisi in Elasticsearch
        verify(mockAracCarisiSearchRepository, times(1)).save(testAracCarisi);
    }

    @Test
    @Transactional
    public void updateNonExistingAracCarisi() throws Exception {
        int databaseSizeBeforeUpdate = aracCarisiRepository.findAll().size();

        // Create the AracCarisi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAracCarisiMockMvc.perform(put("/api/arac-carisis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aracCarisi)))
            .andExpect(status().isBadRequest());

        // Validate the AracCarisi in the database
        List<AracCarisi> aracCarisiList = aracCarisiRepository.findAll();
        assertThat(aracCarisiList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AracCarisi in Elasticsearch
        verify(mockAracCarisiSearchRepository, times(0)).save(aracCarisi);
    }

    @Test
    @Transactional
    public void deleteAracCarisi() throws Exception {
        // Initialize the database
        aracCarisiRepository.saveAndFlush(aracCarisi);

        int databaseSizeBeforeDelete = aracCarisiRepository.findAll().size();

        // Delete the aracCarisi
        restAracCarisiMockMvc.perform(delete("/api/arac-carisis/{id}", aracCarisi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<AracCarisi> aracCarisiList = aracCarisiRepository.findAll();
        assertThat(aracCarisiList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AracCarisi in Elasticsearch
        verify(mockAracCarisiSearchRepository, times(1)).deleteById(aracCarisi.getId());
    }

    @Test
    @Transactional
    public void searchAracCarisi() throws Exception {
        // Initialize the database
        aracCarisiRepository.saveAndFlush(aracCarisi);
        when(mockAracCarisiSearchRepository.search(queryStringQuery("id:" + aracCarisi.getId())))
            .thenReturn(Collections.singletonList(aracCarisi));
        // Search the aracCarisi
        restAracCarisiMockMvc.perform(get("/api/_search/arac-carisis?query=id:" + aracCarisi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aracCarisi.getId().intValue())))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AracCarisi.class);
        AracCarisi aracCarisi1 = new AracCarisi();
        aracCarisi1.setId(1L);
        AracCarisi aracCarisi2 = new AracCarisi();
        aracCarisi2.setId(aracCarisi1.getId());
        assertThat(aracCarisi1).isEqualTo(aracCarisi2);
        aracCarisi2.setId(2L);
        assertThat(aracCarisi1).isNotEqualTo(aracCarisi2);
        aracCarisi1.setId(null);
        assertThat(aracCarisi1).isNotEqualTo(aracCarisi2);
    }
}
