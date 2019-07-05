package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Hesap;
import tr.com.mavi.oto.repository.HesapRepository;
import tr.com.mavi.oto.repository.search.HesapSearchRepository;
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
 * Integration tests for the {@Link HesapResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class HesapResourceIT {

    private static final String DEFAULT_BANKA = "AAAAAAAAAA";
    private static final String UPDATED_BANKA = "BBBBBBBBBB";

    private static final String DEFAULT_SUBE = "AAAAAAAAAA";
    private static final String UPDATED_SUBE = "BBBBBBBBBB";

    private static final String DEFAULT_HESAP_NO = "AAAAAAAAAA";
    private static final String UPDATED_HESAP_NO = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    @Autowired
    private HesapRepository hesapRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.HesapSearchRepositoryMockConfiguration
     */
    @Autowired
    private HesapSearchRepository mockHesapSearchRepository;

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

    private MockMvc restHesapMockMvc;

    private Hesap hesap;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HesapResource hesapResource = new HesapResource(hesapRepository, mockHesapSearchRepository);
        this.restHesapMockMvc = MockMvcBuilders.standaloneSetup(hesapResource)
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
    public static Hesap createEntity(EntityManager em) {
        Hesap hesap = new Hesap()
            .banka(DEFAULT_BANKA)
            .sube(DEFAULT_SUBE)
            .hesapNo(DEFAULT_HESAP_NO)
            .iban(DEFAULT_IBAN);
        return hesap;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hesap createUpdatedEntity(EntityManager em) {
        Hesap hesap = new Hesap()
            .banka(UPDATED_BANKA)
            .sube(UPDATED_SUBE)
            .hesapNo(UPDATED_HESAP_NO)
            .iban(UPDATED_IBAN);
        return hesap;
    }

    @BeforeEach
    public void initTest() {
        hesap = createEntity(em);
    }

    @Test
    @Transactional
    public void createHesap() throws Exception {
        int databaseSizeBeforeCreate = hesapRepository.findAll().size();

        // Create the Hesap
        restHesapMockMvc.perform(post("/api/hesaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hesap)))
            .andExpect(status().isCreated());

        // Validate the Hesap in the database
        List<Hesap> hesapList = hesapRepository.findAll();
        assertThat(hesapList).hasSize(databaseSizeBeforeCreate + 1);
        Hesap testHesap = hesapList.get(hesapList.size() - 1);
        assertThat(testHesap.getBanka()).isEqualTo(DEFAULT_BANKA);
        assertThat(testHesap.getSube()).isEqualTo(DEFAULT_SUBE);
        assertThat(testHesap.getHesapNo()).isEqualTo(DEFAULT_HESAP_NO);
        assertThat(testHesap.getIban()).isEqualTo(DEFAULT_IBAN);

        // Validate the Hesap in Elasticsearch
        verify(mockHesapSearchRepository, times(1)).save(testHesap);
    }

    @Test
    @Transactional
    public void createHesapWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hesapRepository.findAll().size();

        // Create the Hesap with an existing ID
        hesap.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHesapMockMvc.perform(post("/api/hesaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hesap)))
            .andExpect(status().isBadRequest());

        // Validate the Hesap in the database
        List<Hesap> hesapList = hesapRepository.findAll();
        assertThat(hesapList).hasSize(databaseSizeBeforeCreate);

        // Validate the Hesap in Elasticsearch
        verify(mockHesapSearchRepository, times(0)).save(hesap);
    }


    @Test
    @Transactional
    public void getAllHesaps() throws Exception {
        // Initialize the database
        hesapRepository.saveAndFlush(hesap);

        // Get all the hesapList
        restHesapMockMvc.perform(get("/api/hesaps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hesap.getId().intValue())))
            .andExpect(jsonPath("$.[*].banka").value(hasItem(DEFAULT_BANKA.toString())))
            .andExpect(jsonPath("$.[*].sube").value(hasItem(DEFAULT_SUBE.toString())))
            .andExpect(jsonPath("$.[*].hesapNo").value(hasItem(DEFAULT_HESAP_NO.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN.toString())));
    }
    
    @Test
    @Transactional
    public void getHesap() throws Exception {
        // Initialize the database
        hesapRepository.saveAndFlush(hesap);

        // Get the hesap
        restHesapMockMvc.perform(get("/api/hesaps/{id}", hesap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hesap.getId().intValue()))
            .andExpect(jsonPath("$.banka").value(DEFAULT_BANKA.toString()))
            .andExpect(jsonPath("$.sube").value(DEFAULT_SUBE.toString()))
            .andExpect(jsonPath("$.hesapNo").value(DEFAULT_HESAP_NO.toString()))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHesap() throws Exception {
        // Get the hesap
        restHesapMockMvc.perform(get("/api/hesaps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHesap() throws Exception {
        // Initialize the database
        hesapRepository.saveAndFlush(hesap);

        int databaseSizeBeforeUpdate = hesapRepository.findAll().size();

        // Update the hesap
        Hesap updatedHesap = hesapRepository.findById(hesap.getId()).get();
        // Disconnect from session so that the updates on updatedHesap are not directly saved in db
        em.detach(updatedHesap);
        updatedHesap
            .banka(UPDATED_BANKA)
            .sube(UPDATED_SUBE)
            .hesapNo(UPDATED_HESAP_NO)
            .iban(UPDATED_IBAN);

        restHesapMockMvc.perform(put("/api/hesaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHesap)))
            .andExpect(status().isOk());

        // Validate the Hesap in the database
        List<Hesap> hesapList = hesapRepository.findAll();
        assertThat(hesapList).hasSize(databaseSizeBeforeUpdate);
        Hesap testHesap = hesapList.get(hesapList.size() - 1);
        assertThat(testHesap.getBanka()).isEqualTo(UPDATED_BANKA);
        assertThat(testHesap.getSube()).isEqualTo(UPDATED_SUBE);
        assertThat(testHesap.getHesapNo()).isEqualTo(UPDATED_HESAP_NO);
        assertThat(testHesap.getIban()).isEqualTo(UPDATED_IBAN);

        // Validate the Hesap in Elasticsearch
        verify(mockHesapSearchRepository, times(1)).save(testHesap);
    }

    @Test
    @Transactional
    public void updateNonExistingHesap() throws Exception {
        int databaseSizeBeforeUpdate = hesapRepository.findAll().size();

        // Create the Hesap

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHesapMockMvc.perform(put("/api/hesaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hesap)))
            .andExpect(status().isBadRequest());

        // Validate the Hesap in the database
        List<Hesap> hesapList = hesapRepository.findAll();
        assertThat(hesapList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hesap in Elasticsearch
        verify(mockHesapSearchRepository, times(0)).save(hesap);
    }

    @Test
    @Transactional
    public void deleteHesap() throws Exception {
        // Initialize the database
        hesapRepository.saveAndFlush(hesap);

        int databaseSizeBeforeDelete = hesapRepository.findAll().size();

        // Delete the hesap
        restHesapMockMvc.perform(delete("/api/hesaps/{id}", hesap.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hesap> hesapList = hesapRepository.findAll();
        assertThat(hesapList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Hesap in Elasticsearch
        verify(mockHesapSearchRepository, times(1)).deleteById(hesap.getId());
    }

    @Test
    @Transactional
    public void searchHesap() throws Exception {
        // Initialize the database
        hesapRepository.saveAndFlush(hesap);
        when(mockHesapSearchRepository.search(queryStringQuery("id:" + hesap.getId())))
            .thenReturn(Collections.singletonList(hesap));
        // Search the hesap
        restHesapMockMvc.perform(get("/api/_search/hesaps?query=id:" + hesap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hesap.getId().intValue())))
            .andExpect(jsonPath("$.[*].banka").value(hasItem(DEFAULT_BANKA)))
            .andExpect(jsonPath("$.[*].sube").value(hasItem(DEFAULT_SUBE)))
            .andExpect(jsonPath("$.[*].hesapNo").value(hasItem(DEFAULT_HESAP_NO)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hesap.class);
        Hesap hesap1 = new Hesap();
        hesap1.setId(1L);
        Hesap hesap2 = new Hesap();
        hesap2.setId(hesap1.getId());
        assertThat(hesap1).isEqualTo(hesap2);
        hesap2.setId(2L);
        assertThat(hesap1).isNotEqualTo(hesap2);
        hesap1.setId(null);
        assertThat(hesap1).isNotEqualTo(hesap2);
    }
}
