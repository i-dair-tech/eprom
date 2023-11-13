package com.dqlick.eprom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dqlick.eprom.IntegrationTest;
import com.dqlick.eprom.domain.Survey;
import com.dqlick.eprom.repository.SurveyRepository;
import com.dqlick.eprom.service.SurveyService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SurveyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SurveyResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_TYPE = "AAAAAAAAAA";
  private static final String UPDATED_TYPE = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final Integer DEFAULT_QUESTION_ORDER = 1;
  private static final Integer UPDATED_QUESTION_ORDER = 2;

  private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
  private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

  private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_CREATED_DATE = Instant
    .now()
    .truncatedTo(ChronoUnit.MILLIS);

  private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
  private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

  private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(
    0L
  );
  private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant
    .now()
    .truncatedTo(ChronoUnit.MILLIS);

  private static final Boolean DEFAULT_IS_ARCHIVED = false;
  private static final Boolean UPDATED_IS_ARCHIVED = true;

  private static final Instant DEFAULT_ARCHIVED_DATE = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_ARCHIVED_DATE = Instant
    .now()
    .truncatedTo(ChronoUnit.MILLIS);

  private static final String ENTITY_API_URL = "/api/surveys";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private SurveyRepository surveyRepository;

  @Mock
  private SurveyRepository surveyRepositoryMock;

  @Mock
  private SurveyService surveyServiceMock;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restSurveyMockMvc;

  private Survey survey;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Survey createEntity(EntityManager em) {
    Survey survey = new Survey()
      .name(DEFAULT_NAME)
      .type(DEFAULT_TYPE)
      .description(DEFAULT_DESCRIPTION)
      .questionOrder(Collections.singletonList(DEFAULT_QUESTION_ORDER))
      .createdBy(DEFAULT_CREATED_BY)
      .createdDate(DEFAULT_CREATED_DATE)
      .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
      .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
      .isArchived(DEFAULT_IS_ARCHIVED)
      .archivedDate(DEFAULT_ARCHIVED_DATE);
    return survey;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Survey createUpdatedEntity(EntityManager em) {
    Survey survey = new Survey()
      .name(UPDATED_NAME)
      .type(UPDATED_TYPE)
      .description(UPDATED_DESCRIPTION)
      .questionOrder(Collections.singletonList(UPDATED_QUESTION_ORDER))
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);
    return survey;
  }

  @BeforeEach
  public void initTest() {
    survey = createEntity(em);
  }

  @Test
  @Transactional
  void createSurvey() throws Exception {
    int databaseSizeBeforeCreate = surveyRepository.findAll().size();
    // Create the Survey
    restSurveyMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isCreated());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeCreate + 1);
    Survey testSurvey = surveyList.get(surveyList.size() - 1);
    assertThat(testSurvey.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testSurvey.getType()).isEqualTo(DEFAULT_TYPE);
    assertThat(testSurvey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testSurvey.getQuestionOrder()).isEqualTo(DEFAULT_QUESTION_ORDER);
    assertThat(testSurvey.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    assertThat(testSurvey.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    assertThat(testSurvey.getLastModifiedBy())
      .isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    assertThat(testSurvey.getLastModifiedDate())
      .isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    assertThat(testSurvey.getIsArchived()).isEqualTo(DEFAULT_IS_ARCHIVED);
    assertThat(testSurvey.getArchivedDate()).isEqualTo(DEFAULT_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void createSurveyWithExistingId() throws Exception {
    // Create the Survey with an existing ID
    survey.setId(1L);

    int databaseSizeBeforeCreate = surveyRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restSurveyMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isBadRequest());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = surveyRepository.findAll().size();
    // set the field null
    survey.setName(null);

    // Create the Survey, which fails.

    restSurveyMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isBadRequest());

    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkTypeIsRequired() throws Exception {
    int databaseSizeBeforeTest = surveyRepository.findAll().size();
    // set the field null
    survey.setType(null);

    // Create the Survey, which fails.

    restSurveyMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isBadRequest());

    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllSurveys() throws Exception {
    // Initialize the database
    surveyRepository.saveAndFlush(survey);

    // Get all the surveyList
    restSurveyMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId().intValue())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
      .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
      .andExpect(
        jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION))
      )
      .andExpect(
        jsonPath("$.[*].questionOrder").value(hasItem(DEFAULT_QUESTION_ORDER))
      )
      .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
      .andExpect(
        jsonPath("$.[*].createdDate")
          .value(hasItem(DEFAULT_CREATED_DATE.toString()))
      )
      .andExpect(
        jsonPath("$.[*].lastModifiedBy")
          .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
      )
      .andExpect(
        jsonPath("$.[*].lastModifiedDate")
          .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
      )
      .andExpect(
        jsonPath("$.[*].isArchived")
          .value(hasItem(DEFAULT_IS_ARCHIVED.booleanValue()))
      )
      .andExpect(
        jsonPath("$.[*].archivedDate")
          .value(hasItem(DEFAULT_ARCHIVED_DATE.toString()))
      );
  }

  @SuppressWarnings({ "unchecked" })
  void getAllSurveysWithEagerRelationshipsIsEnabled() throws Exception {
    when(surveyServiceMock.findAllWithEagerRelationships(any()))
      .thenReturn(new PageImpl(new ArrayList<>()));

    restSurveyMockMvc
      .perform(get(ENTITY_API_URL + "?eagerload=true"))
      .andExpect(status().isOk());

    verify(surveyServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({ "unchecked" })
  void getAllSurveysWithEagerRelationshipsIsNotEnabled() throws Exception {
    when(surveyServiceMock.findAllWithEagerRelationships(any()))
      .thenReturn(new PageImpl(new ArrayList<>()));

    restSurveyMockMvc
      .perform(get(ENTITY_API_URL + "?eagerload=true"))
      .andExpect(status().isOk());

    verify(surveyServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  void getSurvey() throws Exception {
    // Initialize the database
    surveyRepository.saveAndFlush(survey);

    // Get the survey
    restSurveyMockMvc
      .perform(get(ENTITY_API_URL_ID, survey.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
      .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
      .andExpect(jsonPath("$.questionOrder").value(DEFAULT_QUESTION_ORDER))
      .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
      .andExpect(
        jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString())
      )
      .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
      .andExpect(
        jsonPath("$.lastModifiedDate")
          .value(DEFAULT_LAST_MODIFIED_DATE.toString())
      )
      .andExpect(
        jsonPath("$.isArchived").value(DEFAULT_IS_ARCHIVED.booleanValue())
      )
      .andExpect(
        jsonPath("$.archivedDate").value(DEFAULT_ARCHIVED_DATE.toString())
      );
  }

  @Test
  @Transactional
  void getNonExistingSurvey() throws Exception {
    // Get the survey
    restSurveyMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewSurvey() throws Exception {
    // Initialize the database
    surveyRepository.saveAndFlush(survey);

    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

    // Update the survey
    Survey updatedSurvey = surveyRepository.findById(survey.getId()).get();
    // Disconnect from session so that the updates on updatedSurvey are not directly saved in db
    em.detach(updatedSurvey);
    updatedSurvey
      .name(UPDATED_NAME)
      .type(UPDATED_TYPE)
      .description(UPDATED_DESCRIPTION)
      .questionOrder(Collections.singletonList(UPDATED_QUESTION_ORDER))
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);

    restSurveyMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedSurvey.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedSurvey))
      )
      .andExpect(status().isOk());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    Survey testSurvey = surveyList.get(surveyList.size() - 1);
    assertThat(testSurvey.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testSurvey.getType()).isEqualTo(UPDATED_TYPE);
    assertThat(testSurvey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testSurvey.getQuestionOrder()).isEqualTo(UPDATED_QUESTION_ORDER);
    assertThat(testSurvey.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testSurvey.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testSurvey.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testSurvey.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testSurvey.getIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    assertThat(testSurvey.getArchivedDate()).isEqualTo(UPDATED_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void putNonExistingSurvey() throws Exception {
    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
    survey.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSurveyMockMvc
      .perform(
        put(ENTITY_API_URL_ID, survey.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isBadRequest());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchSurvey() throws Exception {
    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
    survey.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSurveyMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isBadRequest());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamSurvey() throws Exception {
    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
    survey.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSurveyMockMvc
      .perform(
        put(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateSurveyWithPatch() throws Exception {
    // Initialize the database
    surveyRepository.saveAndFlush(survey);

    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

    // Update the survey using partial update
    Survey partialUpdatedSurvey = new Survey();
    partialUpdatedSurvey.setId(survey.getId());

    partialUpdatedSurvey
      .createdBy(UPDATED_CREATED_BY)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

    restSurveyMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSurvey.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurvey))
      )
      .andExpect(status().isOk());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    Survey testSurvey = surveyList.get(surveyList.size() - 1);
    assertThat(testSurvey.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testSurvey.getType()).isEqualTo(DEFAULT_TYPE);
    assertThat(testSurvey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testSurvey.getQuestionOrder()).isEqualTo(DEFAULT_QUESTION_ORDER);
    assertThat(testSurvey.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testSurvey.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    assertThat(testSurvey.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testSurvey.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testSurvey.getIsArchived()).isEqualTo(DEFAULT_IS_ARCHIVED);
    assertThat(testSurvey.getArchivedDate()).isEqualTo(DEFAULT_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void fullUpdateSurveyWithPatch() throws Exception {
    // Initialize the database
    surveyRepository.saveAndFlush(survey);

    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

    // Update the survey using partial update
    Survey partialUpdatedSurvey = new Survey();
    partialUpdatedSurvey.setId(survey.getId());

    partialUpdatedSurvey
      .name(UPDATED_NAME)
      .type(UPDATED_TYPE)
      .description(UPDATED_DESCRIPTION)
      .questionOrder(Collections.singletonList(UPDATED_QUESTION_ORDER))
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);

    restSurveyMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedSurvey.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurvey))
      )
      .andExpect(status().isOk());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    Survey testSurvey = surveyList.get(surveyList.size() - 1);
    assertThat(testSurvey.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testSurvey.getType()).isEqualTo(UPDATED_TYPE);
    assertThat(testSurvey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testSurvey.getQuestionOrder()).isEqualTo(UPDATED_QUESTION_ORDER);
    assertThat(testSurvey.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testSurvey.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testSurvey.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testSurvey.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testSurvey.getIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    assertThat(testSurvey.getArchivedDate()).isEqualTo(UPDATED_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void patchNonExistingSurvey() throws Exception {
    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
    survey.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSurveyMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, survey.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isBadRequest());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchSurvey() throws Exception {
    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
    survey.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSurveyMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isBadRequest());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamSurvey() throws Exception {
    int databaseSizeBeforeUpdate = surveyRepository.findAll().size();
    survey.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restSurveyMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(survey))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the Survey in the database
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteSurvey() throws Exception {
    // Initialize the database
    surveyRepository.saveAndFlush(survey);

    int databaseSizeBeforeDelete = surveyRepository.findAll().size();

    // Delete the survey
    restSurveyMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, survey.getId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Survey> surveyList = surveyRepository.findAll();
    assertThat(surveyList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
