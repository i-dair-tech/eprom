package com.dqlick.eprom.service;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.AnswerChoice;
import com.dqlick.eprom.service.dto.AnswerChoiceDTO;

/**
 * Service Interface for managing {@link AnswerChoice}.
 */
public interface AnswerChoiceService {
    /**
     * Save a answerChoice.
     *
     * @param answerChoiceDTO the entity to save.
     * @return the persisted entity.
     */
    AnswerChoiceDTO save(AnswerChoiceDTO answerChoiceDTO  , String ipAdress );

    /**
     * Updates a answerChoice.
     *
     * @param answerChoice the entity to update.
     * @return the persisted entity.
     */
    AnswerChoice update(AnswerChoice answerChoice);

    /**
     * Partially updates a answerChoice.
     *
     * @param answerChoice the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnswerChoice> partialUpdate(AnswerChoice answerChoice);

    /**
     * Get all the answerChoices.
     *
     * @return the list of entities.
     */
    List<AnswerChoice> findAll();

    /**
     * Get the "id" answerChoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnswerChoice> findOne(Long id);

    /**
     * Delete the "id" answerChoice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
