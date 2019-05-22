package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Marka;
import tr.com.mavi.oto.repository.MarkaRepository;
import tr.com.mavi.oto.repository.search.MarkaSearchRepository;
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
 * Integration tests for the {@Link MarkaResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class MarkaResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    @Autowired
    private MarkaRepository markaRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.MarkaSearchRepositoryMockConfiguration
     */
    @Autowired
    private MarkaSearchRepository mockMarkaSearchRepository;

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

    private MockMvc restMarkaMockMvc;

    private Marka marka;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarkaResource markaResource = new MarkaResource(markaRepository, mockMarkaSearchRepository);
        this.restMarkaMockMvc = MockMvcBuilders.standaloneSetup(markaResource)
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
    public static Marka createEntity(EntityManager em) {
        Marka marka = new Marka()
            .ad(DEFAULT_AD);
        return marka;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marka createUpdatedEntity(EntityManager em) {
        Marka marka = new Marka()
            .ad(UPDATED_AD);
        return marka;
    }

    @BeforeEach
    public void initTest() {
        marka = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarka() throws Exception {
        int databaseSizeBeforeCreate = markaRepository.findAll().size();

        // Create the Marka
        restMarkaMockMvc.perform(post("/api/markas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marka)))
            .andExpect(status().isCreated());

        // Validate the Marka in the database
        List<Marka> markaList = markaRepository.findAll();
        assertThat(markaList).hasSize(databaseSizeBeforeCreate + 1);
        Marka testMarka = markaList.get(markaList.size() - 1);
        assertThat(testMarka.getAd()).isEqualTo(DEFAULT_AD);

        // Validate the Marka in Elasticsearch
        verify(mockMarkaSearchRepository, times(1)).save(testMarka);
    }

    @Test
    @Transactional
    public void createMarkaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = markaRepository.findAll().size();

        // Create the Marka with an existing ID
        marka.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarkaMockMvc.perform(post("/api/markas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marka)))
            .andExpect(status().isBadRequest());

        // Validate the Marka in the database
        List<Marka> markaList = markaRepository.findAll();
        assertThat(markaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Marka in Elasticsearch
        verify(mockMarkaSearchRepository, times(0)).save(marka);
    }


    @Test
    @Transactional
    public void getAllMarkas() throws Exception {
        // Initialize the database
        markaRepository.saveAndFlush(marka);

        // Get all the markaList
        restMarkaMockMvc.perform(get("/api/markas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marka.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())));
    }
    
    @Test
    @Transactional
    public void getMarka() throws Exception {
        // Initialize the database
        markaRepository.saveAndFlush(marka);

        // Get the marka
        restMarkaMockMvc.perform(get("/api/markas/{id}", marka.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marka.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMarka() throws Exception {
        // Get the marka
        restMarkaMockMvc.perform(get("/api/markas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarka() throws Exception {
        // Initialize the database
        markaRepository.saveAndFlush(marka);

        int databaseSizeBeforeUpdate = markaRepository.findAll().size();

        // Update the marka
        Marka updatedMarka = markaRepository.findById(marka.getId()).get();
        // Disconnect from session so that the updates on updatedMarka are not directly saved in db
        em.detach(updatedMarka);
        updatedMarka
            .ad(UPDATED_AD);

        restMarkaMockMvc.perform(put("/api/markas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMarka)))
            .andExpect(status().isOk());

        // Validate the Marka in the database
        List<Marka> markaList = markaRepository.findAll();
        assertThat(markaList).hasSize(databaseSizeBeforeUpdate);
        Marka testMarka = markaList.get(markaList.size() - 1);
        assertThat(testMarka.getAd()).isEqualTo(UPDATED_AD);

        // Validate the Marka in Elasticsearch
        verify(mockMarkaSearchRepository, times(1)).save(testMarka);
    }

    @Test
    @Transactional
    public void updateNonExistingMarka() throws Exception {
        int databaseSizeBeforeUpdate = markaRepository.findAll().size();

        // Create the Marka

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarkaMockMvc.perform(put("/api/markas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marka)))
            .andExpect(status().isBadRequest());

        // Validate the Marka in the database
        List<Marka> markaList = markaRepository.findAll();
        assertThat(markaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Marka in Elasticsearch
        verify(mockMarkaSearchRepository, times(0)).save(marka);
    }

    @Test
    @Transactional
    public void deleteMarka() throws Exception {
        // Initialize the database
        markaRepository.saveAndFlush(marka);

        int databaseSizeBeforeDelete = markaRepository.findAll().size();

        // Delete the marka
        restMarkaMockMvc.perform(delete("/api/markas/{id}", marka.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Marka> markaList = markaRepository.findAll();
        assertThat(markaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Marka in Elasticsearch
        verify(mockMarkaSearchRepository, times(1)).deleteById(marka.getId());
    }

    @Test
    @Transactional
    public void searchMarka() throws Exception {
        // Initialize the database
        markaRepository.saveAndFlush(marka);
        when(mockMarkaSearchRepository.search(queryStringQuery("id:" + marka.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(marka), PageRequest.of(0, 1), 1));
        // Search the marka
        restMarkaMockMvc.perform(get("/api/_search/markas?query=id:" + marka.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marka.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marka.class);
        Marka marka1 = new Marka();
        marka1.setId(1L);
        Marka marka2 = new Marka();
        marka2.setId(marka1.getId());
        assertThat(marka1).isEqualTo(marka2);
        marka2.setId(2L);
        assertThat(marka1).isNotEqualTo(marka2);
        marka1.setId(null);
        assertThat(marka1).isNotEqualTo(marka2);
    }
}
