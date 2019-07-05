package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.IscilikGrubu;
import tr.com.mavi.oto.repository.IscilikGrubuRepository;
import tr.com.mavi.oto.repository.search.IscilikGrubuSearchRepository;
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
 * Integration tests for the {@Link IscilikGrubuResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class IscilikGrubuResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    @Autowired
    private IscilikGrubuRepository iscilikGrubuRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.IscilikGrubuSearchRepositoryMockConfiguration
     */
    @Autowired
    private IscilikGrubuSearchRepository mockIscilikGrubuSearchRepository;

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

    private MockMvc restIscilikGrubuMockMvc;

    private IscilikGrubu iscilikGrubu;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IscilikGrubuResource iscilikGrubuResource = new IscilikGrubuResource(iscilikGrubuRepository, mockIscilikGrubuSearchRepository);
        this.restIscilikGrubuMockMvc = MockMvcBuilders.standaloneSetup(iscilikGrubuResource)
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
    public static IscilikGrubu createEntity(EntityManager em) {
        IscilikGrubu iscilikGrubu = new IscilikGrubu()
            .ad(DEFAULT_AD);
        return iscilikGrubu;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IscilikGrubu createUpdatedEntity(EntityManager em) {
        IscilikGrubu iscilikGrubu = new IscilikGrubu()
            .ad(UPDATED_AD);
        return iscilikGrubu;
    }

    @BeforeEach
    public void initTest() {
        iscilikGrubu = createEntity(em);
    }

    @Test
    @Transactional
    public void createIscilikGrubu() throws Exception {
        int databaseSizeBeforeCreate = iscilikGrubuRepository.findAll().size();

        // Create the IscilikGrubu
        restIscilikGrubuMockMvc.perform(post("/api/iscilik-grubus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilikGrubu)))
            .andExpect(status().isCreated());

        // Validate the IscilikGrubu in the database
        List<IscilikGrubu> iscilikGrubuList = iscilikGrubuRepository.findAll();
        assertThat(iscilikGrubuList).hasSize(databaseSizeBeforeCreate + 1);
        IscilikGrubu testIscilikGrubu = iscilikGrubuList.get(iscilikGrubuList.size() - 1);
        assertThat(testIscilikGrubu.getAd()).isEqualTo(DEFAULT_AD);

        // Validate the IscilikGrubu in Elasticsearch
        verify(mockIscilikGrubuSearchRepository, times(1)).save(testIscilikGrubu);
    }

    @Test
    @Transactional
    public void createIscilikGrubuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = iscilikGrubuRepository.findAll().size();

        // Create the IscilikGrubu with an existing ID
        iscilikGrubu.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIscilikGrubuMockMvc.perform(post("/api/iscilik-grubus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilikGrubu)))
            .andExpect(status().isBadRequest());

        // Validate the IscilikGrubu in the database
        List<IscilikGrubu> iscilikGrubuList = iscilikGrubuRepository.findAll();
        assertThat(iscilikGrubuList).hasSize(databaseSizeBeforeCreate);

        // Validate the IscilikGrubu in Elasticsearch
        verify(mockIscilikGrubuSearchRepository, times(0)).save(iscilikGrubu);
    }


    @Test
    @Transactional
    public void getAllIscilikGrubus() throws Exception {
        // Initialize the database
        iscilikGrubuRepository.saveAndFlush(iscilikGrubu);

        // Get all the iscilikGrubuList
        restIscilikGrubuMockMvc.perform(get("/api/iscilik-grubus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iscilikGrubu.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())));
    }
    
    @Test
    @Transactional
    public void getIscilikGrubu() throws Exception {
        // Initialize the database
        iscilikGrubuRepository.saveAndFlush(iscilikGrubu);

        // Get the iscilikGrubu
        restIscilikGrubuMockMvc.perform(get("/api/iscilik-grubus/{id}", iscilikGrubu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(iscilikGrubu.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIscilikGrubu() throws Exception {
        // Get the iscilikGrubu
        restIscilikGrubuMockMvc.perform(get("/api/iscilik-grubus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIscilikGrubu() throws Exception {
        // Initialize the database
        iscilikGrubuRepository.saveAndFlush(iscilikGrubu);

        int databaseSizeBeforeUpdate = iscilikGrubuRepository.findAll().size();

        // Update the iscilikGrubu
        IscilikGrubu updatedIscilikGrubu = iscilikGrubuRepository.findById(iscilikGrubu.getId()).get();
        // Disconnect from session so that the updates on updatedIscilikGrubu are not directly saved in db
        em.detach(updatedIscilikGrubu);
        updatedIscilikGrubu
            .ad(UPDATED_AD);

        restIscilikGrubuMockMvc.perform(put("/api/iscilik-grubus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIscilikGrubu)))
            .andExpect(status().isOk());

        // Validate the IscilikGrubu in the database
        List<IscilikGrubu> iscilikGrubuList = iscilikGrubuRepository.findAll();
        assertThat(iscilikGrubuList).hasSize(databaseSizeBeforeUpdate);
        IscilikGrubu testIscilikGrubu = iscilikGrubuList.get(iscilikGrubuList.size() - 1);
        assertThat(testIscilikGrubu.getAd()).isEqualTo(UPDATED_AD);

        // Validate the IscilikGrubu in Elasticsearch
        verify(mockIscilikGrubuSearchRepository, times(1)).save(testIscilikGrubu);
    }

    @Test
    @Transactional
    public void updateNonExistingIscilikGrubu() throws Exception {
        int databaseSizeBeforeUpdate = iscilikGrubuRepository.findAll().size();

        // Create the IscilikGrubu

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIscilikGrubuMockMvc.perform(put("/api/iscilik-grubus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilikGrubu)))
            .andExpect(status().isBadRequest());

        // Validate the IscilikGrubu in the database
        List<IscilikGrubu> iscilikGrubuList = iscilikGrubuRepository.findAll();
        assertThat(iscilikGrubuList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IscilikGrubu in Elasticsearch
        verify(mockIscilikGrubuSearchRepository, times(0)).save(iscilikGrubu);
    }

    @Test
    @Transactional
    public void deleteIscilikGrubu() throws Exception {
        // Initialize the database
        iscilikGrubuRepository.saveAndFlush(iscilikGrubu);

        int databaseSizeBeforeDelete = iscilikGrubuRepository.findAll().size();

        // Delete the iscilikGrubu
        restIscilikGrubuMockMvc.perform(delete("/api/iscilik-grubus/{id}", iscilikGrubu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IscilikGrubu> iscilikGrubuList = iscilikGrubuRepository.findAll();
        assertThat(iscilikGrubuList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IscilikGrubu in Elasticsearch
        verify(mockIscilikGrubuSearchRepository, times(1)).deleteById(iscilikGrubu.getId());
    }

    @Test
    @Transactional
    public void searchIscilikGrubu() throws Exception {
        // Initialize the database
        iscilikGrubuRepository.saveAndFlush(iscilikGrubu);
        when(mockIscilikGrubuSearchRepository.search(queryStringQuery("id:" + iscilikGrubu.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(iscilikGrubu), PageRequest.of(0, 1), 1));
        // Search the iscilikGrubu
        restIscilikGrubuMockMvc.perform(get("/api/_search/iscilik-grubus?query=id:" + iscilikGrubu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iscilikGrubu.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IscilikGrubu.class);
        IscilikGrubu iscilikGrubu1 = new IscilikGrubu();
        iscilikGrubu1.setId(1L);
        IscilikGrubu iscilikGrubu2 = new IscilikGrubu();
        iscilikGrubu2.setId(iscilikGrubu1.getId());
        assertThat(iscilikGrubu1).isEqualTo(iscilikGrubu2);
        iscilikGrubu2.setId(2L);
        assertThat(iscilikGrubu1).isNotEqualTo(iscilikGrubu2);
        iscilikGrubu1.setId(null);
        assertThat(iscilikGrubu1).isNotEqualTo(iscilikGrubu2);
    }
}
