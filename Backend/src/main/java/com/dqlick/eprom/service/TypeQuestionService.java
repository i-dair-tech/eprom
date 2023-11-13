package com.dqlick.eprom.service;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.TypeQuestion;

/**
 * Service Interface for managing {@link TypeQuestion}.
 */
public interface TypeQuestionService {
    /**
     * Save a typeQuestion.
     *
     * @param typeQuestion the entity to save.
     * @return the persisted entity.
     */
    TypeQuestion save(TypeQuestion typeQuestion, String ipAdress , String entity);

    /**
     * Updates a typeQuestion.
     *
     * @param typeQuestion the entity to update.
     * @return the persisted entity.
     */
    TypeQuestion update(TypeQuestion typeQuestion, String ipAdress , String entity);

    /**
     * Partially updates a typeQuestion.
     *
     * @param typeQuestion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeQuestion> partialUpdate(TypeQuestion typeQuestion);

    /**
     * Get all the typeQuestions.
     *
     * @return the list of entities.
     */
    List<TypeQuestion> findAll(String ipAdress , String entity);

    /**
     * Get the "id" typeQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeQuestion> findOne(Long id, String ipAdress , String entity);

    /**
     * Delete the "id" typeQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, String ipAdress , String entity);
}
