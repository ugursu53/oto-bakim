package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.IsEmri;
import tr.com.mavi.oto.repository.IsEmriRepository;
import tr.com.mavi.oto.repository.search.IsEmriSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static tr.com.mavi.oto.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import tr.com.mavi.oto.domain.enumeration.IsEmriTipi;
/**
 * Integration tests for the {@Link IsEmriResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class IsEmriResourceIT {

    private static final Instant DEFAULT_GELIS_ZAMANI = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GELIS_ZAMANI = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Instant DEFAULT_KABUL_TARIHI = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_KABUL_TARIHI = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final IsEmriTipi DEFAULT_TIPI = IsEmriTipi.SIGORTA;
    private static final IsEmriTipi UPDATED_TIPI = IsEmriTipi.BAKIM;

    @Autowired
    private IsEmriRepository isEmriRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.IsEmriSearchRepositoryMockConfiguration
     */
    @Autowired
    private IsEmriSearchRepository mockIsEmriSearchRepository;

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

    private MockMvc restIsEmriMockMvc;

    private IsEmri isEmri;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IsEmriResource isEmriResource = new IsEmriResource(isEmriRepository, mockIsEmriSearchRepository);
        this.restIsEmriMockMvc = MockMvcBuilders.standaloneSetup(isEmriResource)
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
    public static IsEmri createEntity(EntityManager em) {
        IsEmri isEmri = new IsEmri()
            .gelisZamani(DEFAULT_GELIS_ZAMANI)
            .aciklama(DEFAULT_ACIKLAMA)
            .kabulTarihi(DEFAULT_KABUL_TARIHI)
            .tipi(DEFAULT_TIPI);
        return isEmri;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsEmri createUpdatedEntity(EntityManager em) {
        IsEmri isEmri = new IsEmri()
            .gelisZamani(UPDATED_GELIS_ZAMANI)
            .aciklama(UPDATED_ACIKLAMA)
            .kabulTarihi(UPDATED_KABUL_TARIHI)
            .tipi(UPDATED_TIPI);
        return isEmri;
    }

    @BeforeEach
    public void initTest() {
        isEmri = createEntity(em);
    }

    @Test
    @Transactional
    public void createIsEmri() throws Exception {
        int databaseSizeBeforeCreate = isEmriRepository.findAll().size();

        // Create the IsEmri
        restIsEmriMockMvc.perform(post("/api/is-emris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(isEmri)))
            .andExpect(status().isCreated());

        // Validate the IsEmri in the database
        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeCreate + 1);
        IsEmri testIsEmri = isEmriList.get(isEmriList.size() - 1);
        assertThat(testIsEmri.getGelisZamani()).isEqualTo(DEFAULT_GELIS_ZAMANI);
        assertThat(testIsEmri.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testIsEmri.getKabulTarihi()).isEqualTo(DEFAULT_KABUL_TARIHI);
        assertThat(testIsEmri.getTipi()).isEqualTo(DEFAULT_TIPI);

        // Validate the IsEmri in Elasticsearch
        verify(mockIsEmriSearchRepository, times(1)).save(testIsEmri);
    }

    @Test
    @Transactional
    public void createIsEmriWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = isEmriRepository.findAll().size();

        // Create the IsEmri with an existing ID
        isEmri.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIsEmriMockMvc.perform(post("/api/is-emris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(isEmri)))
            .andExpect(status().isBadRequest());

        // Validate the IsEmri in the database
        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeCreate);

        // Validate the IsEmri in Elasticsearch
        verify(mockIsEmriSearchRepository, times(0)).save(isEmri);
    }


    @Test
    @Transactional
    public void checkAciklamaIsRequired() throws Exception {
        int databaseSizeBeforeTest = isEmriRepository.findAll().size();
        // set the field null
        isEmri.setAciklama(null);

        // Create the IsEmri, which fails.

        restIsEmriMockMvc.perform(post("/api/is-emris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(isEmri)))
            .andExpect(status().isBadRequest());

        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKabulTarihiIsRequired() throws Exception {
        int databaseSizeBeforeTest = isEmriRepository.findAll().size();
        // set the field null
        isEmri.setKabulTarihi(null);

        // Create the IsEmri, which fails.

        restIsEmriMockMvc.perform(post("/api/is-emris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(isEmri)))
            .andExpect(status().isBadRequest());

        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipiIsRequired() throws Exception {
        int databaseSizeBeforeTest = isEmriRepository.findAll().size();
        // set the field null
        isEmri.setTipi(null);

        // Create the IsEmri, which fails.

        restIsEmriMockMvc.perform(post("/api/is-emris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(isEmri)))
            .andExpect(status().isBadRequest());

        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIsEmris() throws Exception {
        // Initialize the database
        isEmriRepository.saveAndFlush(isEmri);

        // Get all the isEmriList
        restIsEmriMockMvc.perform(get("/api/is-emris?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isEmri.getId().intValue())))
            .andExpect(jsonPath("$.[*].gelisZamani").value(hasItem(DEFAULT_GELIS_ZAMANI.toString())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())))
            .andExpect(jsonPath("$.[*].kabulTarihi").value(hasItem(DEFAULT_KABUL_TARIHI.toString())))
            .andExpect(jsonPath("$.[*].tipi").value(hasItem(DEFAULT_TIPI.toString())));
    }
    
    @Test
    @Transactional
    public void getIsEmri() throws Exception {
        // Initialize the database
        isEmriRepository.saveAndFlush(isEmri);

        // Get the isEmri
        restIsEmriMockMvc.perform(get("/api/is-emris/{id}", isEmri.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(isEmri.getId().intValue()))
            .andExpect(jsonPath("$.gelisZamani").value(DEFAULT_GELIS_ZAMANI.toString()))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()))
            .andExpect(jsonPath("$.kabulTarihi").value(DEFAULT_KABUL_TARIHI.toString()))
            .andExpect(jsonPath("$.tipi").value(DEFAULT_TIPI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIsEmri() throws Exception {
        // Get the isEmri
        restIsEmriMockMvc.perform(get("/api/is-emris/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIsEmri() throws Exception {
        // Initialize the database
        isEmriRepository.saveAndFlush(isEmri);

        int databaseSizeBeforeUpdate = isEmriRepository.findAll().size();

        // Update the isEmri
        IsEmri updatedIsEmri = isEmriRepository.findById(isEmri.getId()).get();
        // Disconnect from session so that the updates on updatedIsEmri are not directly saved in db
        em.detach(updatedIsEmri);
        updatedIsEmri
            .gelisZamani(UPDATED_GELIS_ZAMANI)
            .aciklama(UPDATED_ACIKLAMA)
            .kabulTarihi(UPDATED_KABUL_TARIHI)
            .tipi(UPDATED_TIPI);

        restIsEmriMockMvc.perform(put("/api/is-emris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIsEmri)))
            .andExpect(status().isOk());

        // Validate the IsEmri in the database
        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeUpdate);
        IsEmri testIsEmri = isEmriList.get(isEmriList.size() - 1);
        assertThat(testIsEmri.getGelisZamani()).isEqualTo(UPDATED_GELIS_ZAMANI);
        assertThat(testIsEmri.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testIsEmri.getKabulTarihi()).isEqualTo(UPDATED_KABUL_TARIHI);
        assertThat(testIsEmri.getTipi()).isEqualTo(UPDATED_TIPI);

        // Validate the IsEmri in Elasticsearch
        verify(mockIsEmriSearchRepository, times(1)).save(testIsEmri);
    }

    @Test
    @Transactional
    public void updateNonExistingIsEmri() throws Exception {
        int databaseSizeBeforeUpdate = isEmriRepository.findAll().size();

        // Create the IsEmri

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsEmriMockMvc.perform(put("/api/is-emris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(isEmri)))
            .andExpect(status().isBadRequest());

        // Validate the IsEmri in the database
        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsEmri in Elasticsearch
        verify(mockIsEmriSearchRepository, times(0)).save(isEmri);
    }

    @Test
    @Transactional
    public void deleteIsEmri() throws Exception {
        // Initialize the database
        isEmriRepository.saveAndFlush(isEmri);

        int databaseSizeBeforeDelete = isEmriRepository.findAll().size();

        // Delete the isEmri
        restIsEmriMockMvc.perform(delete("/api/is-emris/{id}", isEmri.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IsEmri> isEmriList = isEmriRepository.findAll();
        assertThat(isEmriList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IsEmri in Elasticsearch
        verify(mockIsEmriSearchRepository, times(1)).deleteById(isEmri.getId());
    }

    @Test
    @Transactional
    public void searchIsEmri() throws Exception {
        // Initialize the database
        isEmriRepository.saveAndFlush(isEmri);
        when(mockIsEmriSearchRepository.search(queryStringQuery("id:" + isEmri.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(isEmri), PageRequest.of(0, 1), 1));
        // Search the isEmri
        restIsEmriMockMvc.perform(get("/api/_search/is-emris?query=id:" + isEmri.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isEmri.getId().intValue())))
            .andExpect(jsonPath("$.[*].gelisZamani").value(hasItem(DEFAULT_GELIS_ZAMANI.toString())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].kabulTarihi").value(hasItem(DEFAULT_KABUL_TARIHI.toString())))
            .andExpect(jsonPath("$.[*].tipi").value(hasItem(DEFAULT_TIPI.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsEmri.class);
        IsEmri isEmri1 = new IsEmri();
        isEmri1.setId(1L);
        IsEmri isEmri2 = new IsEmri();
        isEmri2.setId(isEmri1.getId());
        assertThat(isEmri1).isEqualTo(isEmri2);
        isEmri2.setId(2L);
        assertThat(isEmri1).isNotEqualTo(isEmri2);
        isEmri1.setId(null);
        assertThat(isEmri1).isNotEqualTo(isEmri2);
    }
}
