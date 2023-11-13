package com.dqlick.eprom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dqlick.eprom.IntegrationTest;
import com.dqlick.eprom.domain.Question;
import com.dqlick.eprom.domain.enumeration.Language;
import com.dqlick.eprom.repository.QuestionRepository;
import com.dqlick.eprom.service.QuestionService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

  private static final String DEFAULT_TEXT = "AAAAAAAAAA";
  private static final String UPDATED_TEXT = "BBBBBBBBBB";

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

  private static final Language DEFAULT_LANGUAGE = Language.HINDI;
  private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

  private static final String ENTITY_API_URL = "/api/questions";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private QuestionRepository questionRepository;

  @Mock
  private QuestionRepository questionRepositoryMock;

  @Mock
  private QuestionService questionServiceMock;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restQuestionMockMvc;

  private Question question;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Question createEntity(EntityManager em) {
    Question question = new Question()
      .text(DEFAULT_TEXT)
      .createdBy(DEFAULT_CREATED_BY)
      .createdDate(DEFAULT_CREATED_DATE)
      .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
      .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
      .language(DEFAULT_LANGUAGE);
    return question;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Question createUpdatedEntity(EntityManager em) {
    Question question = new Question()
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .language(UPDATED_LANGUAGE);
    return question;
  }

  @BeforeEach
  public void initTest() {
    question = createEntity(em);
  }

  @Test
  @Transactional
  void createQuestion() throws Exception {
    int databaseSizeBeforeCreate = questionRepository.findAll().size();
    // Create the Question
    restQuestionMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isCreated());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
    Question testQuestion = questionList.get(questionList.size() - 1);
    assertThat(testQuestion.getText()).isEqualTo(DEFAULT_TEXT);
    assertThat(testQuestion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    assertThat(testQuestion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    assertThat(testQuestion.getLastModifiedBy())
      .isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    assertThat(testQuestion.getLastModifiedDate())
      .isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    assertThat(testQuestion.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
  }

  @Test
  @Transactional
  void createQuestionWithExistingId() throws Exception {
    // Create the Question with an existing ID
    question.setId(1L);

    int databaseSizeBeforeCreate = questionRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restQuestionMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isBadRequest());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkTextIsRequired() throws Exception {
    int databaseSizeBeforeTest = questionRepository.findAll().size();
    // set the field null
    question.setText(null);

    // Create the Question, which fails.

    restQuestionMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isBadRequest());

    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void checkLanguageIsRequired() throws Exception {
    int databaseSizeBeforeTest = questionRepository.findAll().size();
    // set the field null
    question.setLanguage(null);

    // Create the Question, which fails.

    restQuestionMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isBadRequest());

    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllQuestions() throws Exception {
    // Initialize the database
    questionRepository.saveAndFlush(question);

    // Get all the questionList
    restQuestionMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(question.getId().intValue()))
      )
      .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
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
      )
      .andExpect(
        jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString()))
      );
  }

  @SuppressWarnings({ "unchecked" })
  void getAllQuestionsWithEagerRelationshipsIsEnabled() throws Exception {
   // when(questionServiceMock.findAllWithEagerRelationships(any()))
    //  .thenReturn(new PageImpl(new ArrayList<>()));

    restQuestionMockMvc
      .perform(get(ENTITY_API_URL + "?eagerload=true"))
      .andExpect(status().isOk());

    //verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({ "unchecked" })
  void getAllQuestionsWithEagerRelationshipsIsNotEnabled() throws Exception {
   // when(questionServiceMock.findAllWithEagerRelationships(any()))
     // .thenReturn(new PageImpl(new ArrayList<>()));

    restQuestionMockMvc
      .perform(get(ENTITY_API_URL + "?eagerload=true"))
      .andExpect(status().isOk());

    //verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  void getQuestion() throws Exception {
    // Initialize the database
    questionRepository.saveAndFlush(question);

    // Get the question
    restQuestionMockMvc
      .perform(get(ENTITY_API_URL_ID, question.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(question.getId().intValue()))
      .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
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
      )
      .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
  }

  @Test
  @Transactional
  void getNonExistingQuestion() throws Exception {
    // Get the question
    restQuestionMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewQuestion() throws Exception {
    // Initialize the database
    questionRepository.saveAndFlush(question);

    int databaseSizeBeforeUpdate = questionRepository.findAll().size();

    // Update the question
    Question updatedQuestion = questionRepository
      .findById(question.getId())
      .get();
    // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
    em.detach(updatedQuestion);
    updatedQuestion
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .language(UPDATED_LANGUAGE);

    restQuestionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedQuestion.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedQuestion))
      )
      .andExpect(status().isOk());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    Question testQuestion = questionList.get(questionList.size() - 1);
    assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testQuestion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testQuestion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testQuestion.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testQuestion.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testQuestion.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
  }

  @Test
  @Transactional
  void putNonExistingQuestion() throws Exception {
    int databaseSizeBeforeUpdate = questionRepository.findAll().size();
    question.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restQuestionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, question.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isBadRequest());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchQuestion() throws Exception {
    int databaseSizeBeforeUpdate = questionRepository.findAll().size();
    question.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restQuestionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isBadRequest());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamQuestion() throws Exception {
    int databaseSizeBeforeUpdate = questionRepository.findAll().size();
    question.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restQuestionMockMvc
      .perform(
        put(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateQuestionWithPatch() throws Exception {
    // Initialize the database
    questionRepository.saveAndFlush(question);

    int databaseSizeBeforeUpdate = questionRepository.findAll().size();

    // Update the question using partial update
    Question partialUpdatedQuestion = new Question();
    partialUpdatedQuestion.setId(question.getId());

    partialUpdatedQuestion
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .language(UPDATED_LANGUAGE);

    restQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
      )
      .andExpect(status().isOk());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    Question testQuestion = questionList.get(questionList.size() - 1);
    assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testQuestion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testQuestion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testQuestion.getLastModifiedBy())
      .isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    assertThat(testQuestion.getLastModifiedDate())
      .isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    assertThat(testQuestion.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
  }

  @Test
  @Transactional
  void fullUpdateQuestionWithPatch() throws Exception {
    // Initialize the database
    questionRepository.saveAndFlush(question);

    int databaseSizeBeforeUpdate = questionRepository.findAll().size();

    // Update the question using partial update
    Question partialUpdatedQuestion = new Question();
    partialUpdatedQuestion.setId(question.getId());

    partialUpdatedQuestion
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .language(UPDATED_LANGUAGE);

    restQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
      )
      .andExpect(status().isOk());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    Question testQuestion = questionList.get(questionList.size() - 1);
    assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testQuestion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testQuestion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testQuestion.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testQuestion.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testQuestion.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
  }

  @Test
  @Transactional
  void patchNonExistingQuestion() throws Exception {
    int databaseSizeBeforeUpdate = questionRepository.findAll().size();
    question.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, question.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isBadRequest());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchQuestion() throws Exception {
    int databaseSizeBeforeUpdate = questionRepository.findAll().size();
    question.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isBadRequest());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamQuestion() throws Exception {
    int databaseSizeBeforeUpdate = questionRepository.findAll().size();
    question.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(question))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the Question in the database
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteQuestion() throws Exception {
    // Initialize the database
    questionRepository.saveAndFlush(question);

    int databaseSizeBeforeDelete = questionRepository.findAll().size();

    // Delete the question
    restQuestionMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, question.getId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Question> questionList = questionRepository.findAll();
    assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
