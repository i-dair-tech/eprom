package com.dqlick.eprom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dqlick.eprom.IntegrationTest;
import com.dqlick.eprom.domain.TypeQuestion;
import com.dqlick.eprom.repository.TypeQuestionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeQuestionResourceIT {

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

  private static final String ENTITY_API_URL = "/api/type-questions";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private TypeQuestionRepository typeQuestionRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restTypeQuestionMockMvc;

  private TypeQuestion typeQuestion;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static TypeQuestion createEntity(EntityManager em) {
    TypeQuestion typeQuestion = new TypeQuestion()
      .text(DEFAULT_TEXT)
      .createdBy(DEFAULT_CREATED_BY)
      .createdDate(DEFAULT_CREATED_DATE)
      .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
      .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
      .isArchived(DEFAULT_IS_ARCHIVED)
      .archivedDate(DEFAULT_ARCHIVED_DATE);
    return typeQuestion;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static TypeQuestion createUpdatedEntity(EntityManager em) {
    TypeQuestion typeQuestion = new TypeQuestion()
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);
    return typeQuestion;
  }

  @BeforeEach
  public void initTest() {
    typeQuestion = createEntity(em);
  }

  @Test
  @Transactional
  void createTypeQuestion() throws Exception {
    int databaseSizeBeforeCreate = typeQuestionRepository.findAll().size();
    // Create the TypeQuestion
    restTypeQuestionMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isCreated());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeCreate + 1);
    TypeQuestion testTypeQuestion = typeQuestionList.get(
      typeQuestionList.size() - 1
    );
    assertThat(testTypeQuestion.getText()).isEqualTo(DEFAULT_TEXT);
    assertThat(testTypeQuestion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    assertThat(testTypeQuestion.getCreatedDate())
      .isEqualTo(DEFAULT_CREATED_DATE);
    assertThat(testTypeQuestion.getLastModifiedBy())
      .isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    assertThat(testTypeQuestion.getLastModifiedDate())
      .isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    assertThat(testTypeQuestion.getIsArchived()).isEqualTo(DEFAULT_IS_ARCHIVED);
    assertThat(testTypeQuestion.getArchivedDate())
      .isEqualTo(DEFAULT_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void createTypeQuestionWithExistingId() throws Exception {
    // Create the TypeQuestion with an existing ID
    typeQuestion.setId(1L);

    int databaseSizeBeforeCreate = typeQuestionRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restTypeQuestionMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isBadRequest());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void checkTextIsRequired() throws Exception {
    int databaseSizeBeforeTest = typeQuestionRepository.findAll().size();
    // set the field null
    typeQuestion.setText(null);

    // Create the TypeQuestion, which fails.

    restTypeQuestionMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isBadRequest());

    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllTypeQuestions() throws Exception {
    // Initialize the database
    typeQuestionRepository.saveAndFlush(typeQuestion);

    // Get all the typeQuestionList
    restTypeQuestionMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(typeQuestion.getId().intValue()))
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
      );
  }

  @Test
  @Transactional
  void getTypeQuestion() throws Exception {
    // Initialize the database
    typeQuestionRepository.saveAndFlush(typeQuestion);

    // Get the typeQuestion
    restTypeQuestionMockMvc
      .perform(get(ENTITY_API_URL_ID, typeQuestion.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(typeQuestion.getId().intValue()))
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
      );
  }

  @Test
  @Transactional
  void getNonExistingTypeQuestion() throws Exception {
    // Get the typeQuestion
    restTypeQuestionMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewTypeQuestion() throws Exception {
    // Initialize the database
    typeQuestionRepository.saveAndFlush(typeQuestion);

    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();

    // Update the typeQuestion
    TypeQuestion updatedTypeQuestion = typeQuestionRepository
      .findById(typeQuestion.getId())
      .get();
    // Disconnect from session so that the updates on updatedTypeQuestion are not directly saved in db
    em.detach(updatedTypeQuestion);
    updatedTypeQuestion
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);

    restTypeQuestionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedTypeQuestion.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedTypeQuestion))
      )
      .andExpect(status().isOk());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
    TypeQuestion testTypeQuestion = typeQuestionList.get(
      typeQuestionList.size() - 1
    );
    assertThat(testTypeQuestion.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testTypeQuestion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testTypeQuestion.getCreatedDate())
      .isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testTypeQuestion.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testTypeQuestion.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testTypeQuestion.getIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    assertThat(testTypeQuestion.getArchivedDate())
      .isEqualTo(UPDATED_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void putNonExistingTypeQuestion() throws Exception {
    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();
    typeQuestion.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restTypeQuestionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, typeQuestion.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isBadRequest());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchTypeQuestion() throws Exception {
    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();
    typeQuestion.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restTypeQuestionMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isBadRequest());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamTypeQuestion() throws Exception {
    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();
    typeQuestion.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restTypeQuestionMockMvc
      .perform(
        put(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateTypeQuestionWithPatch() throws Exception {
    // Initialize the database
    typeQuestionRepository.saveAndFlush(typeQuestion);

    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();

    // Update the typeQuestion using partial update
    TypeQuestion partialUpdatedTypeQuestion = new TypeQuestion();
    partialUpdatedTypeQuestion.setId(typeQuestion.getId());

    partialUpdatedTypeQuestion
      .text(UPDATED_TEXT)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

    restTypeQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedTypeQuestion.getId())
          .contentType("application/merge-patch+json")
          .content(
            TestUtil.convertObjectToJsonBytes(partialUpdatedTypeQuestion)
          )
      )
      .andExpect(status().isOk());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
    TypeQuestion testTypeQuestion = typeQuestionList.get(
      typeQuestionList.size() - 1
    );
    assertThat(testTypeQuestion.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testTypeQuestion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    assertThat(testTypeQuestion.getCreatedDate())
      .isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testTypeQuestion.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testTypeQuestion.getLastModifiedDate())
      .isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    assertThat(testTypeQuestion.getIsArchived()).isEqualTo(DEFAULT_IS_ARCHIVED);
    assertThat(testTypeQuestion.getArchivedDate())
      .isEqualTo(DEFAULT_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void fullUpdateTypeQuestionWithPatch() throws Exception {
    // Initialize the database
    typeQuestionRepository.saveAndFlush(typeQuestion);

    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();

    // Update the typeQuestion using partial update
    TypeQuestion partialUpdatedTypeQuestion = new TypeQuestion();
    partialUpdatedTypeQuestion.setId(typeQuestion.getId());

    partialUpdatedTypeQuestion
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);

    restTypeQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedTypeQuestion.getId())
          .contentType("application/merge-patch+json")
          .content(
            TestUtil.convertObjectToJsonBytes(partialUpdatedTypeQuestion)
          )
      )
      .andExpect(status().isOk());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
    TypeQuestion testTypeQuestion = typeQuestionList.get(
      typeQuestionList.size() - 1
    );
    assertThat(testTypeQuestion.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testTypeQuestion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testTypeQuestion.getCreatedDate())
      .isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testTypeQuestion.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testTypeQuestion.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testTypeQuestion.getIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    assertThat(testTypeQuestion.getArchivedDate())
      .isEqualTo(UPDATED_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void patchNonExistingTypeQuestion() throws Exception {
    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();
    typeQuestion.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restTypeQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, typeQuestion.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isBadRequest());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchTypeQuestion() throws Exception {
    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();
    typeQuestion.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restTypeQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isBadRequest());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamTypeQuestion() throws Exception {
    int databaseSizeBeforeUpdate = typeQuestionRepository.findAll().size();
    typeQuestion.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restTypeQuestionMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(typeQuestion))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the TypeQuestion in the database
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteTypeQuestion() throws Exception {
    // Initialize the database
    typeQuestionRepository.saveAndFlush(typeQuestion);

    int databaseSizeBeforeDelete = typeQuestionRepository.findAll().size();

    // Delete the typeQuestion
    restTypeQuestionMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, typeQuestion.getId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<TypeQuestion> typeQuestionList = typeQuestionRepository.findAll();
    assertThat(typeQuestionList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
