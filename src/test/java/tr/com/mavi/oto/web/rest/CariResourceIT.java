package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Cari;
import tr.com.mavi.oto.repository.CariRepository;
import tr.com.mavi.oto.repository.search.CariSearchRepository;
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

import tr.com.mavi.oto.domain.enumeration.CariTipi;
import tr.com.mavi.oto.domain.enumeration.KisiTipi;
import tr.com.mavi.oto.domain.enumeration.IsEmriTipi;
/**
 * Integration tests for the {@Link CariResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class CariResourceIT {

    private static final CariTipi DEFAULT_TIPI = CariTipi.MUSTERI;
    private static final CariTipi UPDATED_TIPI = CariTipi.TEDARIKCI;

    private static final KisiTipi DEFAULT_KISI_TIPI = KisiTipi.GERCEK;
    private static final KisiTipi UPDATED_KISI_TIPI = KisiTipi.TUZEL;

    private static final Boolean DEFAULT_AKTIF = false;
    private static final Boolean UPDATED_AKTIF = true;

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final String DEFAULT_ADRES = "AAAAAAAAAA";
    private static final String UPDATED_ADRES = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON = "BBBBBBBBBB";

    private static final String DEFAULT_TC_NO = "AAAAAAAAAA";
    private static final String UPDATED_TC_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VERGI_NO = "AAAAAAAAAA";
    private static final String UPDATED_VERGI_NO = "BBBBBBBBBB";

    private static final String DEFAULT_YETKILI = "AAAAAAAAAA";
    private static final String UPDATED_YETKILI = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_EPOSTA = "AAAAAAAAAA";
    private static final String UPDATED_EPOSTA = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_ADRESI = "AAAAAAAAAA";
    private static final String UPDATED_WEB_ADRESI = "BBBBBBBBBB";

    private static final Double DEFAULT_ISKONTO = 1D;
    private static final Double UPDATED_ISKONTO = 2D;

    private static final Boolean DEFAULT_EFATURA_KULLANIMI = false;
    private static final Boolean UPDATED_EFATURA_KULLANIMI = true;

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final IsEmriTipi DEFAULT_VARSAYILAN_IS_EMRI_TIPI = IsEmriTipi.SIGORTA;
    private static final IsEmriTipi UPDATED_VARSAYILAN_IS_EMRI_TIPI = IsEmriTipi.BAKIM;

    @Autowired
    private CariRepository cariRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.CariSearchRepositoryMockConfiguration
     */
    @Autowired
    private CariSearchRepository mockCariSearchRepository;

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

    private MockMvc restCariMockMvc;

    private Cari cari;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CariResource cariResource = new CariResource(cariRepository, mockCariSearchRepository);
        this.restCariMockMvc = MockMvcBuilders.standaloneSetup(cariResource)
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
    public static Cari createEntity(EntityManager em) {
        Cari cari = new Cari()
            .tipi(DEFAULT_TIPI)
            .kisiTipi(DEFAULT_KISI_TIPI)
            .aktif(DEFAULT_AKTIF)
            .ad(DEFAULT_AD)
            .adres(DEFAULT_ADRES)
            .telefon(DEFAULT_TELEFON)
            .tcNo(DEFAULT_TC_NO)
            .vergiNo(DEFAULT_VERGI_NO)
            .yetkili(DEFAULT_YETKILI)
            .fax(DEFAULT_FAX)
            .eposta(DEFAULT_EPOSTA)
            .webAdresi(DEFAULT_WEB_ADRESI)
            .iskonto(DEFAULT_ISKONTO)
            .efaturaKullanimi(DEFAULT_EFATURA_KULLANIMI)
            .aciklama(DEFAULT_ACIKLAMA)
            .varsayilanIsEmriTipi(DEFAULT_VARSAYILAN_IS_EMRI_TIPI);
        return cari;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cari createUpdatedEntity(EntityManager em) {
        Cari cari = new Cari()
            .tipi(UPDATED_TIPI)
            .kisiTipi(UPDATED_KISI_TIPI)
            .aktif(UPDATED_AKTIF)
            .ad(UPDATED_AD)
            .adres(UPDATED_ADRES)
            .telefon(UPDATED_TELEFON)
            .tcNo(UPDATED_TC_NO)
            .vergiNo(UPDATED_VERGI_NO)
            .yetkili(UPDATED_YETKILI)
            .fax(UPDATED_FAX)
            .eposta(UPDATED_EPOSTA)
            .webAdresi(UPDATED_WEB_ADRESI)
            .iskonto(UPDATED_ISKONTO)
            .efaturaKullanimi(UPDATED_EFATURA_KULLANIMI)
            .aciklama(UPDATED_ACIKLAMA)
            .varsayilanIsEmriTipi(UPDATED_VARSAYILAN_IS_EMRI_TIPI);
        return cari;
    }

    @BeforeEach
    public void initTest() {
        cari = createEntity(em);
    }

    @Test
    @Transactional
    public void createCari() throws Exception {
        int databaseSizeBeforeCreate = cariRepository.findAll().size();

        // Create the Cari
        restCariMockMvc.perform(post("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cari)))
            .andExpect(status().isCreated());

        // Validate the Cari in the database
        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeCreate + 1);
        Cari testCari = cariList.get(cariList.size() - 1);
        assertThat(testCari.getTipi()).isEqualTo(DEFAULT_TIPI);
        assertThat(testCari.getKisiTipi()).isEqualTo(DEFAULT_KISI_TIPI);
        assertThat(testCari.isAktif()).isEqualTo(DEFAULT_AKTIF);
        assertThat(testCari.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testCari.getAdres()).isEqualTo(DEFAULT_ADRES);
        assertThat(testCari.getTelefon()).isEqualTo(DEFAULT_TELEFON);
        assertThat(testCari.getTcNo()).isEqualTo(DEFAULT_TC_NO);
        assertThat(testCari.getVergiNo()).isEqualTo(DEFAULT_VERGI_NO);
        assertThat(testCari.getYetkili()).isEqualTo(DEFAULT_YETKILI);
        assertThat(testCari.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testCari.getEposta()).isEqualTo(DEFAULT_EPOSTA);
        assertThat(testCari.getWebAdresi()).isEqualTo(DEFAULT_WEB_ADRESI);
        assertThat(testCari.getIskonto()).isEqualTo(DEFAULT_ISKONTO);
        assertThat(testCari.isEfaturaKullanimi()).isEqualTo(DEFAULT_EFATURA_KULLANIMI);
        assertThat(testCari.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testCari.getVarsayilanIsEmriTipi()).isEqualTo(DEFAULT_VARSAYILAN_IS_EMRI_TIPI);

        // Validate the Cari in Elasticsearch
        verify(mockCariSearchRepository, times(1)).save(testCari);
    }

    @Test
    @Transactional
    public void createCariWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cariRepository.findAll().size();

        // Create the Cari with an existing ID
        cari.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCariMockMvc.perform(post("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cari)))
            .andExpect(status().isBadRequest());

        // Validate the Cari in the database
        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cari in Elasticsearch
        verify(mockCariSearchRepository, times(0)).save(cari);
    }


    @Test
    @Transactional
    public void checkTipiIsRequired() throws Exception {
        int databaseSizeBeforeTest = cariRepository.findAll().size();
        // set the field null
        cari.setTipi(null);

        // Create the Cari, which fails.

        restCariMockMvc.perform(post("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cari)))
            .andExpect(status().isBadRequest());

        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKisiTipiIsRequired() throws Exception {
        int databaseSizeBeforeTest = cariRepository.findAll().size();
        // set the field null
        cari.setKisiTipi(null);

        // Create the Cari, which fails.

        restCariMockMvc.perform(post("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cari)))
            .andExpect(status().isBadRequest());

        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAktifIsRequired() throws Exception {
        int databaseSizeBeforeTest = cariRepository.findAll().size();
        // set the field null
        cari.setAktif(null);

        // Create the Cari, which fails.

        restCariMockMvc.perform(post("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cari)))
            .andExpect(status().isBadRequest());

        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cariRepository.findAll().size();
        // set the field null
        cari.setAd(null);

        // Create the Cari, which fails.

        restCariMockMvc.perform(post("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cari)))
            .andExpect(status().isBadRequest());

        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCaris() throws Exception {
        // Initialize the database
        cariRepository.saveAndFlush(cari);

        // Get all the cariList
        restCariMockMvc.perform(get("/api/caris?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cari.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipi").value(hasItem(DEFAULT_TIPI.toString())))
            .andExpect(jsonPath("$.[*].kisiTipi").value(hasItem(DEFAULT_KISI_TIPI.toString())))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())))
            .andExpect(jsonPath("$.[*].adres").value(hasItem(DEFAULT_ADRES.toString())))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON.toString())))
            .andExpect(jsonPath("$.[*].tcNo").value(hasItem(DEFAULT_TC_NO.toString())))
            .andExpect(jsonPath("$.[*].vergiNo").value(hasItem(DEFAULT_VERGI_NO.toString())))
            .andExpect(jsonPath("$.[*].yetkili").value(hasItem(DEFAULT_YETKILI.toString())))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX.toString())))
            .andExpect(jsonPath("$.[*].eposta").value(hasItem(DEFAULT_EPOSTA.toString())))
            .andExpect(jsonPath("$.[*].webAdresi").value(hasItem(DEFAULT_WEB_ADRESI.toString())))
            .andExpect(jsonPath("$.[*].iskonto").value(hasItem(DEFAULT_ISKONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].efaturaKullanimi").value(hasItem(DEFAULT_EFATURA_KULLANIMI.booleanValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())))
            .andExpect(jsonPath("$.[*].varsayilanIsEmriTipi").value(hasItem(DEFAULT_VARSAYILAN_IS_EMRI_TIPI.toString())));
    }
    
    @Test
    @Transactional
    public void getCari() throws Exception {
        // Initialize the database
        cariRepository.saveAndFlush(cari);

        // Get the cari
        restCariMockMvc.perform(get("/api/caris/{id}", cari.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cari.getId().intValue()))
            .andExpect(jsonPath("$.tipi").value(DEFAULT_TIPI.toString()))
            .andExpect(jsonPath("$.kisiTipi").value(DEFAULT_KISI_TIPI.toString()))
            .andExpect(jsonPath("$.aktif").value(DEFAULT_AKTIF.booleanValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()))
            .andExpect(jsonPath("$.adres").value(DEFAULT_ADRES.toString()))
            .andExpect(jsonPath("$.telefon").value(DEFAULT_TELEFON.toString()))
            .andExpect(jsonPath("$.tcNo").value(DEFAULT_TC_NO.toString()))
            .andExpect(jsonPath("$.vergiNo").value(DEFAULT_VERGI_NO.toString()))
            .andExpect(jsonPath("$.yetkili").value(DEFAULT_YETKILI.toString()))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX.toString()))
            .andExpect(jsonPath("$.eposta").value(DEFAULT_EPOSTA.toString()))
            .andExpect(jsonPath("$.webAdresi").value(DEFAULT_WEB_ADRESI.toString()))
            .andExpect(jsonPath("$.iskonto").value(DEFAULT_ISKONTO.doubleValue()))
            .andExpect(jsonPath("$.efaturaKullanimi").value(DEFAULT_EFATURA_KULLANIMI.booleanValue()))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()))
            .andExpect(jsonPath("$.varsayilanIsEmriTipi").value(DEFAULT_VARSAYILAN_IS_EMRI_TIPI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCari() throws Exception {
        // Get the cari
        restCariMockMvc.perform(get("/api/caris/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCari() throws Exception {
        // Initialize the database
        cariRepository.saveAndFlush(cari);

        int databaseSizeBeforeUpdate = cariRepository.findAll().size();

        // Update the cari
        Cari updatedCari = cariRepository.findById(cari.getId()).get();
        // Disconnect from session so that the updates on updatedCari are not directly saved in db
        em.detach(updatedCari);
        updatedCari
            .tipi(UPDATED_TIPI)
            .kisiTipi(UPDATED_KISI_TIPI)
            .aktif(UPDATED_AKTIF)
            .ad(UPDATED_AD)
            .adres(UPDATED_ADRES)
            .telefon(UPDATED_TELEFON)
            .tcNo(UPDATED_TC_NO)
            .vergiNo(UPDATED_VERGI_NO)
            .yetkili(UPDATED_YETKILI)
            .fax(UPDATED_FAX)
            .eposta(UPDATED_EPOSTA)
            .webAdresi(UPDATED_WEB_ADRESI)
            .iskonto(UPDATED_ISKONTO)
            .efaturaKullanimi(UPDATED_EFATURA_KULLANIMI)
            .aciklama(UPDATED_ACIKLAMA)
            .varsayilanIsEmriTipi(UPDATED_VARSAYILAN_IS_EMRI_TIPI);

        restCariMockMvc.perform(put("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCari)))
            .andExpect(status().isOk());

        // Validate the Cari in the database
        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeUpdate);
        Cari testCari = cariList.get(cariList.size() - 1);
        assertThat(testCari.getTipi()).isEqualTo(UPDATED_TIPI);
        assertThat(testCari.getKisiTipi()).isEqualTo(UPDATED_KISI_TIPI);
        assertThat(testCari.isAktif()).isEqualTo(UPDATED_AKTIF);
        assertThat(testCari.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testCari.getAdres()).isEqualTo(UPDATED_ADRES);
        assertThat(testCari.getTelefon()).isEqualTo(UPDATED_TELEFON);
        assertThat(testCari.getTcNo()).isEqualTo(UPDATED_TC_NO);
        assertThat(testCari.getVergiNo()).isEqualTo(UPDATED_VERGI_NO);
        assertThat(testCari.getYetkili()).isEqualTo(UPDATED_YETKILI);
        assertThat(testCari.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testCari.getEposta()).isEqualTo(UPDATED_EPOSTA);
        assertThat(testCari.getWebAdresi()).isEqualTo(UPDATED_WEB_ADRESI);
        assertThat(testCari.getIskonto()).isEqualTo(UPDATED_ISKONTO);
        assertThat(testCari.isEfaturaKullanimi()).isEqualTo(UPDATED_EFATURA_KULLANIMI);
        assertThat(testCari.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testCari.getVarsayilanIsEmriTipi()).isEqualTo(UPDATED_VARSAYILAN_IS_EMRI_TIPI);

        // Validate the Cari in Elasticsearch
        verify(mockCariSearchRepository, times(1)).save(testCari);
    }

    @Test
    @Transactional
    public void updateNonExistingCari() throws Exception {
        int databaseSizeBeforeUpdate = cariRepository.findAll().size();

        // Create the Cari

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCariMockMvc.perform(put("/api/caris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cari)))
            .andExpect(status().isBadRequest());

        // Validate the Cari in the database
        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cari in Elasticsearch
        verify(mockCariSearchRepository, times(0)).save(cari);
    }

    @Test
    @Transactional
    public void deleteCari() throws Exception {
        // Initialize the database
        cariRepository.saveAndFlush(cari);

        int databaseSizeBeforeDelete = cariRepository.findAll().size();

        // Delete the cari
        restCariMockMvc.perform(delete("/api/caris/{id}", cari.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Cari> cariList = cariRepository.findAll();
        assertThat(cariList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cari in Elasticsearch
        verify(mockCariSearchRepository, times(1)).deleteById(cari.getId());
    }

    @Test
    @Transactional
    public void searchCari() throws Exception {
        // Initialize the database
        cariRepository.saveAndFlush(cari);
        when(mockCariSearchRepository.search(queryStringQuery("id:" + cari.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cari), PageRequest.of(0, 1), 1));
        // Search the cari
        restCariMockMvc.perform(get("/api/_search/caris?query=id:" + cari.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cari.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipi").value(hasItem(DEFAULT_TIPI.toString())))
            .andExpect(jsonPath("$.[*].kisiTipi").value(hasItem(DEFAULT_KISI_TIPI.toString())))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].adres").value(hasItem(DEFAULT_ADRES)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)))
            .andExpect(jsonPath("$.[*].tcNo").value(hasItem(DEFAULT_TC_NO)))
            .andExpect(jsonPath("$.[*].vergiNo").value(hasItem(DEFAULT_VERGI_NO)))
            .andExpect(jsonPath("$.[*].yetkili").value(hasItem(DEFAULT_YETKILI)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].eposta").value(hasItem(DEFAULT_EPOSTA)))
            .andExpect(jsonPath("$.[*].webAdresi").value(hasItem(DEFAULT_WEB_ADRESI)))
            .andExpect(jsonPath("$.[*].iskonto").value(hasItem(DEFAULT_ISKONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].efaturaKullanimi").value(hasItem(DEFAULT_EFATURA_KULLANIMI.booleanValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].varsayilanIsEmriTipi").value(hasItem(DEFAULT_VARSAYILAN_IS_EMRI_TIPI.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cari.class);
        Cari cari1 = new Cari();
        cari1.setId(1L);
        Cari cari2 = new Cari();
        cari2.setId(cari1.getId());
        assertThat(cari1).isEqualTo(cari2);
        cari2.setId(2L);
        assertThat(cari1).isNotEqualTo(cari2);
        cari1.setId(null);
        assertThat(cari1).isNotEqualTo(cari2);
    }
}
