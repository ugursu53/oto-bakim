package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Personel;
import tr.com.mavi.oto.repository.PersonelRepository;
import tr.com.mavi.oto.repository.search.PersonelSearchRepository;
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
 * Integration tests for the {@Link PersonelResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class PersonelResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final String DEFAULT_SOYAD = "AAAAAAAAAA";
    private static final String UPDATED_SOYAD = "BBBBBBBBBB";

    private static final String DEFAULT_GOREVI = "AAAAAAAAAA";
    private static final String UPDATED_GOREVI = "BBBBBBBBBB";

    @Autowired
    private PersonelRepository personelRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.PersonelSearchRepositoryMockConfiguration
     */
    @Autowired
    private PersonelSearchRepository mockPersonelSearchRepository;

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

    private MockMvc restPersonelMockMvc;

    private Personel personel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersonelResource personelResource = new PersonelResource(personelRepository, mockPersonelSearchRepository);
        this.restPersonelMockMvc = MockMvcBuilders.standaloneSetup(personelResource)
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
    public static Personel createEntity(EntityManager em) {
        Personel personel = new Personel()
            .ad(DEFAULT_AD)
            .soyad(DEFAULT_SOYAD)
            .gorevi(DEFAULT_GOREVI);
        return personel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personel createUpdatedEntity(EntityManager em) {
        Personel personel = new Personel()
            .ad(UPDATED_AD)
            .soyad(UPDATED_SOYAD)
            .gorevi(UPDATED_GOREVI);
        return personel;
    }

    @BeforeEach
    public void initTest() {
        personel = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonel() throws Exception {
        int databaseSizeBeforeCreate = personelRepository.findAll().size();

        // Create the Personel
        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isCreated());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeCreate + 1);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testPersonel.getSoyad()).isEqualTo(DEFAULT_SOYAD);
        assertThat(testPersonel.getGorevi()).isEqualTo(DEFAULT_GOREVI);

        // Validate the Personel in Elasticsearch
        verify(mockPersonelSearchRepository, times(1)).save(testPersonel);
    }

    @Test
    @Transactional
    public void createPersonelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personelRepository.findAll().size();

        // Create the Personel with an existing ID
        personel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeCreate);

        // Validate the Personel in Elasticsearch
        verify(mockPersonelSearchRepository, times(0)).save(personel);
    }


    @Test
    @Transactional
    public void checkAdIsRequired() throws Exception {
        int databaseSizeBeforeTest = personelRepository.findAll().size();
        // set the field null
        personel.setAd(null);

        // Create the Personel, which fails.

        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSoyadIsRequired() throws Exception {
        int databaseSizeBeforeTest = personelRepository.findAll().size();
        // set the field null
        personel.setSoyad(null);

        // Create the Personel, which fails.

        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGoreviIsRequired() throws Exception {
        int databaseSizeBeforeTest = personelRepository.findAll().size();
        // set the field null
        personel.setGorevi(null);

        // Create the Personel, which fails.

        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonels() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        // Get all the personelList
        restPersonelMockMvc.perform(get("/api/personels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personel.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())))
            .andExpect(jsonPath("$.[*].soyad").value(hasItem(DEFAULT_SOYAD.toString())))
            .andExpect(jsonPath("$.[*].gorevi").value(hasItem(DEFAULT_GOREVI.toString())));
    }
    
    @Test
    @Transactional
    public void getPersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        // Get the personel
        restPersonelMockMvc.perform(get("/api/personels/{id}", personel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personel.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()))
            .andExpect(jsonPath("$.soyad").value(DEFAULT_SOYAD.toString()))
            .andExpect(jsonPath("$.gorevi").value(DEFAULT_GOREVI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonel() throws Exception {
        // Get the personel
        restPersonelMockMvc.perform(get("/api/personels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeUpdate = personelRepository.findAll().size();

        // Update the personel
        Personel updatedPersonel = personelRepository.findById(personel.getId()).get();
        // Disconnect from session so that the updates on updatedPersonel are not directly saved in db
        em.detach(updatedPersonel);
        updatedPersonel
            .ad(UPDATED_AD)
            .soyad(UPDATED_SOYAD)
            .gorevi(UPDATED_GOREVI);

        restPersonelMockMvc.perform(put("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersonel)))
            .andExpect(status().isOk());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testPersonel.getSoyad()).isEqualTo(UPDATED_SOYAD);
        assertThat(testPersonel.getGorevi()).isEqualTo(UPDATED_GOREVI);

        // Validate the Personel in Elasticsearch
        verify(mockPersonelSearchRepository, times(1)).save(testPersonel);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();

        // Create the Personel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonelMockMvc.perform(put("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Personel in Elasticsearch
        verify(mockPersonelSearchRepository, times(0)).save(personel);
    }

    @Test
    @Transactional
    public void deletePersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeDelete = personelRepository.findAll().size();

        // Delete the personel
        restPersonelMockMvc.perform(delete("/api/personels/{id}", personel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Personel in Elasticsearch
        verify(mockPersonelSearchRepository, times(1)).deleteById(personel.getId());
    }

    @Test
    @Transactional
    public void searchPersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);
        when(mockPersonelSearchRepository.search(queryStringQuery("id:" + personel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(personel), PageRequest.of(0, 1), 1));
        // Search the personel
        restPersonelMockMvc.perform(get("/api/_search/personels?query=id:" + personel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personel.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].soyad").value(hasItem(DEFAULT_SOYAD)))
            .andExpect(jsonPath("$.[*].gorevi").value(hasItem(DEFAULT_GOREVI)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personel.class);
        Personel personel1 = new Personel();
        personel1.setId(1L);
        Personel personel2 = new Personel();
        personel2.setId(personel1.getId());
        assertThat(personel1).isEqualTo(personel2);
        personel2.setId(2L);
        assertThat(personel1).isNotEqualTo(personel2);
        personel1.setId(null);
        assertThat(personel1).isNotEqualTo(personel2);
    }
}
