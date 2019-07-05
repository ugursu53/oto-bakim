package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.OtoBakimApp;
import tr.com.mavi.oto.domain.Model;
import tr.com.mavi.oto.repository.ModelRepository;
import tr.com.mavi.oto.repository.search.ModelSearchRepository;
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
 * Integration tests for the {@Link ModelResource} REST controller.
 */
@SpringBootTest(classes = OtoBakimApp.class)
public class ModelResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    @Autowired
    private ModelRepository modelRepository;

    /**
     * This repository is mocked in the tr.com.mavi.oto.repository.search test package.
     *
     * @see tr.com.mavi.oto.repository.search.ModelSearchRepositoryMockConfiguration
     */
    @Autowired
    private ModelSearchRepository mockModelSearchRepository;

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

    private MockMvc restModelMockMvc;

    private Model model;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModelResource modelResource = new ModelResource(modelRepository, mockModelSearchRepository);
        this.restModelMockMvc = MockMvcBuilders.standaloneSetup(modelResource)
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
    public static Model createEntity(EntityManager em) {
        Model model = new Model()
            .ad(DEFAULT_AD);
        return model;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createUpdatedEntity(EntityManager em) {
        Model model = new Model()
            .ad(UPDATED_AD);
        return model;
    }

    @BeforeEach
    public void initTest() {
        model = createEntity(em);
    }

    @Test
    @Transactional
    public void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model
        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getAd()).isEqualTo(DEFAULT_AD);

        // Validate the Model in Elasticsearch
        verify(mockModelSearchRepository, times(1)).save(testModel);
    }

    @Test
    @Transactional
    public void createModelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model with an existing ID
        model.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate);

        // Validate the Model in Elasticsearch
        verify(mockModelSearchRepository, times(0)).save(model);
    }


    @Test
    @Transactional
    public void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList
        restModelMockMvc.perform(get("/api/models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())));
    }
    
    @Test
    @Transactional
    public void getModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(model.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model
        Model updatedModel = modelRepository.findById(model.getId()).get();
        // Disconnect from session so that the updates on updatedModel are not directly saved in db
        em.detach(updatedModel);
        updatedModel
            .ad(UPDATED_AD);

        restModelMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModel)))
            .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getAd()).isEqualTo(UPDATED_AD);

        // Validate the Model in Elasticsearch
        verify(mockModelSearchRepository, times(1)).save(testModel);
    }

    @Test
    @Transactional
    public void updateNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Create the Model

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(model)))
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Model in Elasticsearch
        verify(mockModelSearchRepository, times(0)).save(model);
    }

    @Test
    @Transactional
    public void deleteModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        int databaseSizeBeforeDelete = modelRepository.findAll().size();

        // Delete the model
        restModelMockMvc.perform(delete("/api/models/{id}", model.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Model> modelList = modelRepository.findAll();
        assertThat(modelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Model in Elasticsearch
        verify(mockModelSearchRepository, times(1)).deleteById(model.getId());
    }

    @Test
    @Transactional
    public void searchModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);
        when(mockModelSearchRepository.search(queryStringQuery("id:" + model.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(model), PageRequest.of(0, 1), 1));
        // Search the model
        restModelMockMvc.perform(get("/api/_search/models?query=id:" + model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Model.class);
        Model model1 = new Model();
        model1.setId(1L);
        Model model2 = new Model();
        model2.setId(model1.getId());
        assertThat(model1).isEqualTo(model2);
        model2.setId(2L);
        assertThat(model1).isNotEqualTo(model2);
        model1.setId(null);
        assertThat(model1).isNotEqualTo(model2);
    }
}
