package com.dqlick.eprom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dqlick.eprom.IntegrationTest;
import com.dqlick.eprom.domain.AnswerChoice;
import com.dqlick.eprom.repository.AnswerChoiceRepository;
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
 * Integration tests for the {@link AnswerChoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnswerChoiceResourceIT {

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

  private static final String ENTITY_API_URL = "/api/answer-choices";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  @Autowired
  private AnswerChoiceRepository answerChoiceRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restAnswerChoiceMockMvc;

  private AnswerChoice answerChoice;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AnswerChoice createEntity(EntityManager em) {
    AnswerChoice answerChoice = new AnswerChoice()
      .text(DEFAULT_TEXT)
      .createdBy(DEFAULT_CREATED_BY)
      .createdDate(DEFAULT_CREATED_DATE)
      .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
      .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
      .isArchived(DEFAULT_IS_ARCHIVED)
      .archivedDate(DEFAULT_ARCHIVED_DATE);
    return answerChoice;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static AnswerChoice createUpdatedEntity(EntityManager em) {
    AnswerChoice answerChoice = new AnswerChoice()
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);
    return answerChoice;
  }

  @BeforeEach
  public void initTest() {
    answerChoice = createEntity(em);
  }

  @Test
  @Transactional
  void createAnswerChoice() throws Exception {
    int databaseSizeBeforeCreate = answerChoiceRepository.findAll().size();
    // Create the AnswerChoice
    restAnswerChoiceMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isCreated());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeCreate + 1);
    AnswerChoice testAnswerChoice = answerChoiceList.get(
      answerChoiceList.size() - 1
    );
    assertThat(testAnswerChoice.getText()).isEqualTo(DEFAULT_TEXT);
    assertThat(testAnswerChoice.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    assertThat(testAnswerChoice.getCreatedDate())
      .isEqualTo(DEFAULT_CREATED_DATE);
    assertThat(testAnswerChoice.getLastModifiedBy())
      .isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    assertThat(testAnswerChoice.getLastModifiedDate())
      .isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    assertThat(testAnswerChoice.getIsArchived()).isEqualTo(DEFAULT_IS_ARCHIVED);
    assertThat(testAnswerChoice.getArchivedDate())
      .isEqualTo(DEFAULT_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void createAnswerChoiceWithExistingId() throws Exception {
    // Create the AnswerChoice with an existing ID
    answerChoice.setId(1L);

    int databaseSizeBeforeCreate = answerChoiceRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restAnswerChoiceMockMvc
      .perform(
        post(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isBadRequest());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllAnswerChoices() throws Exception {
    // Initialize the database
    answerChoiceRepository.saveAndFlush(answerChoice);

    // Get all the answerChoiceList
    restAnswerChoiceMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(
        jsonPath("$.[*].id").value(hasItem(answerChoice.getId().intValue()))
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
  void getAnswerChoice() throws Exception {
    // Initialize the database
    answerChoiceRepository.saveAndFlush(answerChoice);

    // Get the answerChoice
    restAnswerChoiceMockMvc
      .perform(get(ENTITY_API_URL_ID, answerChoice.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(answerChoice.getId().intValue()))
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
  void getNonExistingAnswerChoice() throws Exception {
    // Get the answerChoice
    restAnswerChoiceMockMvc
      .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewAnswerChoice() throws Exception {
    // Initialize the database
    answerChoiceRepository.saveAndFlush(answerChoice);

    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();

    // Update the answerChoice
    AnswerChoice updatedAnswerChoice = answerChoiceRepository
      .findById(answerChoice.getId())
      .get();
    // Disconnect from session so that the updates on updatedAnswerChoice are not directly saved in db
    em.detach(updatedAnswerChoice);
    updatedAnswerChoice
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);

    restAnswerChoiceMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedAnswerChoice.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedAnswerChoice))
      )
      .andExpect(status().isOk());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
    AnswerChoice testAnswerChoice = answerChoiceList.get(
      answerChoiceList.size() - 1
    );
    assertThat(testAnswerChoice.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testAnswerChoice.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testAnswerChoice.getCreatedDate())
      .isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testAnswerChoice.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testAnswerChoice.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testAnswerChoice.getIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    assertThat(testAnswerChoice.getArchivedDate())
      .isEqualTo(UPDATED_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void putNonExistingAnswerChoice() throws Exception {
    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();
    answerChoice.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAnswerChoiceMockMvc
      .perform(
        put(ENTITY_API_URL_ID, answerChoice.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isBadRequest());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchAnswerChoice() throws Exception {
    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();
    answerChoice.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAnswerChoiceMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isBadRequest());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamAnswerChoice() throws Exception {
    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();
    answerChoice.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAnswerChoiceMockMvc
      .perform(
        put(ENTITY_API_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateAnswerChoiceWithPatch() throws Exception {
    // Initialize the database
    answerChoiceRepository.saveAndFlush(answerChoice);

    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();

    // Update the answerChoice using partial update
    AnswerChoice partialUpdatedAnswerChoice = new AnswerChoice();
    partialUpdatedAnswerChoice.setId(answerChoice.getId());

    partialUpdatedAnswerChoice
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);

    restAnswerChoiceMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAnswerChoice.getId())
          .contentType("application/merge-patch+json")
          .content(
            TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerChoice)
          )
      )
      .andExpect(status().isOk());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
    AnswerChoice testAnswerChoice = answerChoiceList.get(
      answerChoiceList.size() - 1
    );
    assertThat(testAnswerChoice.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testAnswerChoice.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testAnswerChoice.getCreatedDate())
      .isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testAnswerChoice.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testAnswerChoice.getLastModifiedDate())
      .isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    assertThat(testAnswerChoice.getIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    assertThat(testAnswerChoice.getArchivedDate())
      .isEqualTo(UPDATED_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void fullUpdateAnswerChoiceWithPatch() throws Exception {
    // Initialize the database
    answerChoiceRepository.saveAndFlush(answerChoice);

    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();

    // Update the answerChoice using partial update
    AnswerChoice partialUpdatedAnswerChoice = new AnswerChoice();
    partialUpdatedAnswerChoice.setId(answerChoice.getId());

    partialUpdatedAnswerChoice
      .text(UPDATED_TEXT)
      .createdBy(UPDATED_CREATED_BY)
      .createdDate(UPDATED_CREATED_DATE)
      .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
      .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
      .isArchived(UPDATED_IS_ARCHIVED)
      .archivedDate(UPDATED_ARCHIVED_DATE);

    restAnswerChoiceMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedAnswerChoice.getId())
          .contentType("application/merge-patch+json")
          .content(
            TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerChoice)
          )
      )
      .andExpect(status().isOk());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
    AnswerChoice testAnswerChoice = answerChoiceList.get(
      answerChoiceList.size() - 1
    );
    assertThat(testAnswerChoice.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testAnswerChoice.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testAnswerChoice.getCreatedDate())
      .isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testAnswerChoice.getLastModifiedBy())
      .isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testAnswerChoice.getLastModifiedDate())
      .isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    assertThat(testAnswerChoice.getIsArchived()).isEqualTo(UPDATED_IS_ARCHIVED);
    assertThat(testAnswerChoice.getArchivedDate())
      .isEqualTo(UPDATED_ARCHIVED_DATE);
  }

  @Test
  @Transactional
  void patchNonExistingAnswerChoice() throws Exception {
    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();
    answerChoice.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAnswerChoiceMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, answerChoice.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isBadRequest());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchAnswerChoice() throws Exception {
    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();
    answerChoice.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAnswerChoiceMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isBadRequest());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamAnswerChoice() throws Exception {
    int databaseSizeBeforeUpdate = answerChoiceRepository.findAll().size();
    answerChoice.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restAnswerChoiceMockMvc
      .perform(
        patch(ENTITY_API_URL)
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(answerChoice))
      )
      .andExpect(status().isMethodNotAllowed());

    // Validate the AnswerChoice in the database
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteAnswerChoice() throws Exception {
    // Initialize the database
    answerChoiceRepository.saveAndFlush(answerChoice);

    int databaseSizeBeforeDelete = answerChoiceRepository.findAll().size();

    // Delete the answerChoice
    restAnswerChoiceMockMvc
      .perform(
        delete(ENTITY_API_URL_ID, answerChoice.getId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<AnswerChoice> answerChoiceList = answerChoiceRepository.findAll();
    assertThat(answerChoiceList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
