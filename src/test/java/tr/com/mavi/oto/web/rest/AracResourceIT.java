package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Arac;
import tr.com.mavi.oto.repository.AracRepository;
import tr.com.mavi.oto.repository.search.AracSearchRepository;
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

import tr.com.mavi.oto.domain.enumeration.YakitTuru;
import tr.com.mavi.oto.domain.enumeration.VitesTuru;
import tr.com.mavi.oto.domain.enumeration.KullanimSekli;
import tr.com.mavi.oto.domain.enumeration.AracTipi;
/**
 * Integration tests for the {@Link AracResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class AracResourceIT {

    private static final String DEFAULT_PLAKA_NO = "AAAAAAAAAA";
    private static final String UPDATED_PLAKA_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_MODEL_YILI = 1;
    private static final Integer UPDATED_MODEL_YILI = 2;

    private static final String DEFAULT_RENGI = "AAAAAAAAAA";
    private static final String UPDATED_RENGI = "BBBBBBBBBB";

    private static final YakitTuru DEFAULT_YAKIT_TURU = YakitTuru.BENZIN;
    private static final YakitTuru UPDATED_YAKIT_TURU = YakitTuru.DIZEL;

    private static final VitesTuru DEFAULT_VITES_TURU = VitesTuru.MANUEL;
    private static final VitesTuru UPDATED_VITES_TURU = VitesTuru.YARI_OTOMATIK;

    private static final String DEFAULT_MOTOR_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOTOR_NO = "BBBBBBBBBB";

    private static final String DEFAULT_SASI_NO = "AAAAAAAAAA";
    private static final String UPDATED_SASI_NO = "BBBBBBBBBB";

    private static final KullanimSekli DEFAULT_KULLANIM_SEKLI = KullanimSekli.HUSISI;
    private static final KullanimSekli UPDATED_KULLANIM_SEKLI = KullanimSekli.SIRKET;

    private static final AracTipi DEFAULT_ARAC_TIPI = AracTipi.HATCHBACK;
    private static final AracTipi UPDATED_ARAC_TIPI = AracTipi.SEDAN;

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    @Autowired
    private AracRepository aracRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.AracSearchRepositoryMockConfiguration
     */
    @Autowired
    private AracSearchRepository mockAracSearchRepository;

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

    private MockMvc restAracMockMvc;

    private Arac arac;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AracResource aracResource = new AracResource(aracRepository, mockAracSearchRepository);
        this.restAracMockMvc = MockMvcBuilders.standaloneSetup(aracResource)
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
    public static Arac createEntity(EntityManager em) {
        Arac arac = new Arac()
            .plakaNo(DEFAULT_PLAKA_NO)
            .modelYili(DEFAULT_MODEL_YILI)
            .rengi(DEFAULT_RENGI)
            .yakitTuru(DEFAULT_YAKIT_TURU)
            .vitesTuru(DEFAULT_VITES_TURU)
            .motorNo(DEFAULT_MOTOR_NO)
            .sasiNo(DEFAULT_SASI_NO)
            .kullanimSekli(DEFAULT_KULLANIM_SEKLI)
            .aracTipi(DEFAULT_ARAC_TIPI)
            .aciklama(DEFAULT_ACIKLAMA);
        return arac;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arac createUpdatedEntity(EntityManager em) {
        Arac arac = new Arac()
            .plakaNo(UPDATED_PLAKA_NO)
            .modelYili(UPDATED_MODEL_YILI)
            .rengi(UPDATED_RENGI)
            .yakitTuru(UPDATED_YAKIT_TURU)
            .vitesTuru(UPDATED_VITES_TURU)
            .motorNo(UPDATED_MOTOR_NO)
            .sasiNo(UPDATED_SASI_NO)
            .kullanimSekli(UPDATED_KULLANIM_SEKLI)
            .aracTipi(UPDATED_ARAC_TIPI)
            .aciklama(UPDATED_ACIKLAMA);
        return arac;
    }

    @BeforeEach
    public void initTest() {
        arac = createEntity(em);
    }

    @Test
    @Transactional
    public void createArac() throws Exception {
        int databaseSizeBeforeCreate = aracRepository.findAll().size();

        // Create the Arac
        restAracMockMvc.perform(post("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arac)))
            .andExpect(status().isCreated());

        // Validate the Arac in the database
        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeCreate + 1);
        Arac testArac = aracList.get(aracList.size() - 1);
        assertThat(testArac.getPlakaNo()).isEqualTo(DEFAULT_PLAKA_NO);
        assertThat(testArac.getModelYili()).isEqualTo(DEFAULT_MODEL_YILI);
        assertThat(testArac.getRengi()).isEqualTo(DEFAULT_RENGI);
        assertThat(testArac.getYakitTuru()).isEqualTo(DEFAULT_YAKIT_TURU);
        assertThat(testArac.getVitesTuru()).isEqualTo(DEFAULT_VITES_TURU);
        assertThat(testArac.getMotorNo()).isEqualTo(DEFAULT_MOTOR_NO);
        assertThat(testArac.getSasiNo()).isEqualTo(DEFAULT_SASI_NO);
        assertThat(testArac.getKullanimSekli()).isEqualTo(DEFAULT_KULLANIM_SEKLI);
        assertThat(testArac.getAracTipi()).isEqualTo(DEFAULT_ARAC_TIPI);
        assertThat(testArac.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);

        // Validate the Arac in Elasticsearch
        verify(mockAracSearchRepository, times(1)).save(testArac);
    }

    @Test
    @Transactional
    public void createAracWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aracRepository.findAll().size();

        // Create the Arac with an existing ID
        arac.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAracMockMvc.perform(post("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arac)))
            .andExpect(status().isBadRequest());

        // Validate the Arac in the database
        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeCreate);

        // Validate the Arac in Elasticsearch
        verify(mockAracSearchRepository, times(0)).save(arac);
    }


    @Test
    @Transactional
    public void checkPlakaNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = aracRepository.findAll().size();
        // set the field null
        arac.setPlakaNo(null);

        // Create the Arac, which fails.

        restAracMockMvc.perform(post("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arac)))
            .andExpect(status().isBadRequest());

        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModelYiliIsRequired() throws Exception {
        int databaseSizeBeforeTest = aracRepository.findAll().size();
        // set the field null
        arac.setModelYili(null);

        // Create the Arac, which fails.

        restAracMockMvc.perform(post("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arac)))
            .andExpect(status().isBadRequest());

        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYakitTuruIsRequired() throws Exception {
        int databaseSizeBeforeTest = aracRepository.findAll().size();
        // set the field null
        arac.setYakitTuru(null);

        // Create the Arac, which fails.

        restAracMockMvc.perform(post("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arac)))
            .andExpect(status().isBadRequest());

        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVitesTuruIsRequired() throws Exception {
        int databaseSizeBeforeTest = aracRepository.findAll().size();
        // set the field null
        arac.setVitesTuru(null);

        // Create the Arac, which fails.

        restAracMockMvc.perform(post("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arac)))
            .andExpect(status().isBadRequest());

        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAracs() throws Exception {
        // Initialize the database
        aracRepository.saveAndFlush(arac);

        // Get all the aracList
        restAracMockMvc.perform(get("/api/aracs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arac.getId().intValue())))
            .andExpect(jsonPath("$.[*].plakaNo").value(hasItem(DEFAULT_PLAKA_NO.toString())))
            .andExpect(jsonPath("$.[*].modelYili").value(hasItem(DEFAULT_MODEL_YILI)))
            .andExpect(jsonPath("$.[*].rengi").value(hasItem(DEFAULT_RENGI.toString())))
            .andExpect(jsonPath("$.[*].yakitTuru").value(hasItem(DEFAULT_YAKIT_TURU.toString())))
            .andExpect(jsonPath("$.[*].vitesTuru").value(hasItem(DEFAULT_VITES_TURU.toString())))
            .andExpect(jsonPath("$.[*].motorNo").value(hasItem(DEFAULT_MOTOR_NO.toString())))
            .andExpect(jsonPath("$.[*].sasiNo").value(hasItem(DEFAULT_SASI_NO.toString())))
            .andExpect(jsonPath("$.[*].kullanimSekli").value(hasItem(DEFAULT_KULLANIM_SEKLI.toString())))
            .andExpect(jsonPath("$.[*].aracTipi").value(hasItem(DEFAULT_ARAC_TIPI.toString())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())));
    }
    
    @Test
    @Transactional
    public void getArac() throws Exception {
        // Initialize the database
        aracRepository.saveAndFlush(arac);

        // Get the arac
        restAracMockMvc.perform(get("/api/aracs/{id}", arac.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(arac.getId().intValue()))
            .andExpect(jsonPath("$.plakaNo").value(DEFAULT_PLAKA_NO.toString()))
            .andExpect(jsonPath("$.modelYili").value(DEFAULT_MODEL_YILI))
            .andExpect(jsonPath("$.rengi").value(DEFAULT_RENGI.toString()))
            .andExpect(jsonPath("$.yakitTuru").value(DEFAULT_YAKIT_TURU.toString()))
            .andExpect(jsonPath("$.vitesTuru").value(DEFAULT_VITES_TURU.toString()))
            .andExpect(jsonPath("$.motorNo").value(DEFAULT_MOTOR_NO.toString()))
            .andExpect(jsonPath("$.sasiNo").value(DEFAULT_SASI_NO.toString()))
            .andExpect(jsonPath("$.kullanimSekli").value(DEFAULT_KULLANIM_SEKLI.toString()))
            .andExpect(jsonPath("$.aracTipi").value(DEFAULT_ARAC_TIPI.toString()))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArac() throws Exception {
        // Get the arac
        restAracMockMvc.perform(get("/api/aracs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArac() throws Exception {
        // Initialize the database
        aracRepository.saveAndFlush(arac);

        int databaseSizeBeforeUpdate = aracRepository.findAll().size();

        // Update the arac
        Arac updatedArac = aracRepository.findById(arac.getId()).get();
        // Disconnect from session so that the updates on updatedArac are not directly saved in db
        em.detach(updatedArac);
        updatedArac
            .plakaNo(UPDATED_PLAKA_NO)
            .modelYili(UPDATED_MODEL_YILI)
            .rengi(UPDATED_RENGI)
            .yakitTuru(UPDATED_YAKIT_TURU)
            .vitesTuru(UPDATED_VITES_TURU)
            .motorNo(UPDATED_MOTOR_NO)
            .sasiNo(UPDATED_SASI_NO)
            .kullanimSekli(UPDATED_KULLANIM_SEKLI)
            .aracTipi(UPDATED_ARAC_TIPI)
            .aciklama(UPDATED_ACIKLAMA);

        restAracMockMvc.perform(put("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArac)))
            .andExpect(status().isOk());

        // Validate the Arac in the database
        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeUpdate);
        Arac testArac = aracList.get(aracList.size() - 1);
        assertThat(testArac.getPlakaNo()).isEqualTo(UPDATED_PLAKA_NO);
        assertThat(testArac.getModelYili()).isEqualTo(UPDATED_MODEL_YILI);
        assertThat(testArac.getRengi()).isEqualTo(UPDATED_RENGI);
        assertThat(testArac.getYakitTuru()).isEqualTo(UPDATED_YAKIT_TURU);
        assertThat(testArac.getVitesTuru()).isEqualTo(UPDATED_VITES_TURU);
        assertThat(testArac.getMotorNo()).isEqualTo(UPDATED_MOTOR_NO);
        assertThat(testArac.getSasiNo()).isEqualTo(UPDATED_SASI_NO);
        assertThat(testArac.getKullanimSekli()).isEqualTo(UPDATED_KULLANIM_SEKLI);
        assertThat(testArac.getAracTipi()).isEqualTo(UPDATED_ARAC_TIPI);
        assertThat(testArac.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);

        // Validate the Arac in Elasticsearch
        verify(mockAracSearchRepository, times(1)).save(testArac);
    }

    @Test
    @Transactional
    public void updateNonExistingArac() throws Exception {
        int databaseSizeBeforeUpdate = aracRepository.findAll().size();

        // Create the Arac

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAracMockMvc.perform(put("/api/aracs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arac)))
            .andExpect(status().isBadRequest());

        // Validate the Arac in the database
        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Arac in Elasticsearch
        verify(mockAracSearchRepository, times(0)).save(arac);
    }

    @Test
    @Transactional
    public void deleteArac() throws Exception {
        // Initialize the database
        aracRepository.saveAndFlush(arac);

        int databaseSizeBeforeDelete = aracRepository.findAll().size();

        // Delete the arac
        restAracMockMvc.perform(delete("/api/aracs/{id}", arac.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Arac> aracList = aracRepository.findAll();
        assertThat(aracList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Arac in Elasticsearch
        verify(mockAracSearchRepository, times(1)).deleteById(arac.getId());
    }

    @Test
    @Transactional
    public void searchArac() throws Exception {
        // Initialize the database
        aracRepository.saveAndFlush(arac);
        when(mockAracSearchRepository.search(queryStringQuery("id:" + arac.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(arac), PageRequest.of(0, 1), 1));
        // Search the arac
        restAracMockMvc.perform(get("/api/_search/aracs?query=id:" + arac.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arac.getId().intValue())))
            .andExpect(jsonPath("$.[*].plakaNo").value(hasItem(DEFAULT_PLAKA_NO)))
            .andExpect(jsonPath("$.[*].modelYili").value(hasItem(DEFAULT_MODEL_YILI)))
            .andExpect(jsonPath("$.[*].rengi").value(hasItem(DEFAULT_RENGI)))
            .andExpect(jsonPath("$.[*].yakitTuru").value(hasItem(DEFAULT_YAKIT_TURU.toString())))
            .andExpect(jsonPath("$.[*].vitesTuru").value(hasItem(DEFAULT_VITES_TURU.toString())))
            .andExpect(jsonPath("$.[*].motorNo").value(hasItem(DEFAULT_MOTOR_NO)))
            .andExpect(jsonPath("$.[*].sasiNo").value(hasItem(DEFAULT_SASI_NO)))
            .andExpect(jsonPath("$.[*].kullanimSekli").value(hasItem(DEFAULT_KULLANIM_SEKLI.toString())))
            .andExpect(jsonPath("$.[*].aracTipi").value(hasItem(DEFAULT_ARAC_TIPI.toString())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arac.class);
        Arac arac1 = new Arac();
        arac1.setId(1L);
        Arac arac2 = new Arac();
        arac2.setId(arac1.getId());
        assertThat(arac1).isEqualTo(arac2);
        arac2.setId(2L);
        assertThat(arac1).isNotEqualTo(arac2);
        arac1.setId(null);
        assertThat(arac1).isNotEqualTo(arac2);
    }
}
