package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Iscilik;
import tr.com.mavi.oto.repository.IscilikRepository;
import tr.com.mavi.oto.repository.search.IscilikSearchRepository;
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
 * Integration tests for the {@Link IscilikResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class IscilikResourceIT {

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Double DEFAULT_FIYAT = 1D;
    private static final Double UPDATED_FIYAT = 2D;

    private static final Double DEFAULT_ISKONTO = 1D;
    private static final Double UPDATED_ISKONTO = 2D;

    @Autowired
    private IscilikRepository iscilikRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.IscilikSearchRepositoryMockConfiguration
     */
    @Autowired
    private IscilikSearchRepository mockIscilikSearchRepository;

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

    private MockMvc restIscilikMockMvc;

    private Iscilik iscilik;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IscilikResource iscilikResource = new IscilikResource(iscilikRepository, mockIscilikSearchRepository);
        this.restIscilikMockMvc = MockMvcBuilders.standaloneSetup(iscilikResource)
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
    public static Iscilik createEntity(EntityManager em) {
        Iscilik iscilik = new Iscilik()
            .aciklama(DEFAULT_ACIKLAMA)
            .fiyat(DEFAULT_FIYAT)
            .iskonto(DEFAULT_ISKONTO);
        return iscilik;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iscilik createUpdatedEntity(EntityManager em) {
        Iscilik iscilik = new Iscilik()
            .aciklama(UPDATED_ACIKLAMA)
            .fiyat(UPDATED_FIYAT)
            .iskonto(UPDATED_ISKONTO);
        return iscilik;
    }

    @BeforeEach
    public void initTest() {
        iscilik = createEntity(em);
    }

    @Test
    @Transactional
    public void createIscilik() throws Exception {
        int databaseSizeBeforeCreate = iscilikRepository.findAll().size();

        // Create the Iscilik
        restIscilikMockMvc.perform(post("/api/isciliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilik)))
            .andExpect(status().isCreated());

        // Validate the Iscilik in the database
        List<Iscilik> iscilikList = iscilikRepository.findAll();
        assertThat(iscilikList).hasSize(databaseSizeBeforeCreate + 1);
        Iscilik testIscilik = iscilikList.get(iscilikList.size() - 1);
        assertThat(testIscilik.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testIscilik.getFiyat()).isEqualTo(DEFAULT_FIYAT);
        assertThat(testIscilik.getIskonto()).isEqualTo(DEFAULT_ISKONTO);

        // Validate the Iscilik in Elasticsearch
        verify(mockIscilikSearchRepository, times(1)).save(testIscilik);
    }

    @Test
    @Transactional
    public void createIscilikWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = iscilikRepository.findAll().size();

        // Create the Iscilik with an existing ID
        iscilik.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIscilikMockMvc.perform(post("/api/isciliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilik)))
            .andExpect(status().isBadRequest());

        // Validate the Iscilik in the database
        List<Iscilik> iscilikList = iscilikRepository.findAll();
        assertThat(iscilikList).hasSize(databaseSizeBeforeCreate);

        // Validate the Iscilik in Elasticsearch
        verify(mockIscilikSearchRepository, times(0)).save(iscilik);
    }


    @Test
    @Transactional
    public void getAllIsciliks() throws Exception {
        // Initialize the database
        iscilikRepository.saveAndFlush(iscilik);

        // Get all the iscilikList
        restIscilikMockMvc.perform(get("/api/isciliks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iscilik.getId().intValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())))
            .andExpect(jsonPath("$.[*].fiyat").value(hasItem(DEFAULT_FIYAT.doubleValue())))
            .andExpect(jsonPath("$.[*].iskonto").value(hasItem(DEFAULT_ISKONTO.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getIscilik() throws Exception {
        // Initialize the database
        iscilikRepository.saveAndFlush(iscilik);

        // Get the iscilik
        restIscilikMockMvc.perform(get("/api/isciliks/{id}", iscilik.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(iscilik.getId().intValue()))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()))
            .andExpect(jsonPath("$.fiyat").value(DEFAULT_FIYAT.doubleValue()))
            .andExpect(jsonPath("$.iskonto").value(DEFAULT_ISKONTO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIscilik() throws Exception {
        // Get the iscilik
        restIscilikMockMvc.perform(get("/api/isciliks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIscilik() throws Exception {
        // Initialize the database
        iscilikRepository.saveAndFlush(iscilik);

        int databaseSizeBeforeUpdate = iscilikRepository.findAll().size();

        // Update the iscilik
        Iscilik updatedIscilik = iscilikRepository.findById(iscilik.getId()).get();
        // Disconnect from session so that the updates on updatedIscilik are not directly saved in db
        em.detach(updatedIscilik);
        updatedIscilik
            .aciklama(UPDATED_ACIKLAMA)
            .fiyat(UPDATED_FIYAT)
            .iskonto(UPDATED_ISKONTO);

        restIscilikMockMvc.perform(put("/api/isciliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIscilik)))
            .andExpect(status().isOk());

        // Validate the Iscilik in the database
        List<Iscilik> iscilikList = iscilikRepository.findAll();
        assertThat(iscilikList).hasSize(databaseSizeBeforeUpdate);
        Iscilik testIscilik = iscilikList.get(iscilikList.size() - 1);
        assertThat(testIscilik.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testIscilik.getFiyat()).isEqualTo(UPDATED_FIYAT);
        assertThat(testIscilik.getIskonto()).isEqualTo(UPDATED_ISKONTO);

        // Validate the Iscilik in Elasticsearch
        verify(mockIscilikSearchRepository, times(1)).save(testIscilik);
    }

    @Test
    @Transactional
    public void updateNonExistingIscilik() throws Exception {
        int databaseSizeBeforeUpdate = iscilikRepository.findAll().size();

        // Create the Iscilik

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIscilikMockMvc.perform(put("/api/isciliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iscilik)))
            .andExpect(status().isBadRequest());

        // Validate the Iscilik in the database
        List<Iscilik> iscilikList = iscilikRepository.findAll();
        assertThat(iscilikList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Iscilik in Elasticsearch
        verify(mockIscilikSearchRepository, times(0)).save(iscilik);
    }

    @Test
    @Transactional
    public void deleteIscilik() throws Exception {
        // Initialize the database
        iscilikRepository.saveAndFlush(iscilik);

        int databaseSizeBeforeDelete = iscilikRepository.findAll().size();

        // Delete the iscilik
        restIscilikMockMvc.perform(delete("/api/isciliks/{id}", iscilik.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Iscilik> iscilikList = iscilikRepository.findAll();
        assertThat(iscilikList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Iscilik in Elasticsearch
        verify(mockIscilikSearchRepository, times(1)).deleteById(iscilik.getId());
    }

    @Test
    @Transactional
    public void searchIscilik() throws Exception {
        // Initialize the database
        iscilikRepository.saveAndFlush(iscilik);
        when(mockIscilikSearchRepository.search(queryStringQuery("id:" + iscilik.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(iscilik), PageRequest.of(0, 1), 1));
        // Search the iscilik
        restIscilikMockMvc.perform(get("/api/_search/isciliks?query=id:" + iscilik.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iscilik.getId().intValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].fiyat").value(hasItem(DEFAULT_FIYAT.doubleValue())))
            .andExpect(jsonPath("$.[*].iskonto").value(hasItem(DEFAULT_ISKONTO.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Iscilik.class);
        Iscilik iscilik1 = new Iscilik();
        iscilik1.setId(1L);
        Iscilik iscilik2 = new Iscilik();
        iscilik2.setId(iscilik1.getId());
        assertThat(iscilik1).isEqualTo(iscilik2);
        iscilik2.setId(2L);
        assertThat(iscilik1).isNotEqualTo(iscilik2);
        iscilik1.setId(null);
        assertThat(iscilik1).isNotEqualTo(iscilik2);
    }
}
