package com.dqlick.eprom.service;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.Answer;
import com.dqlick.eprom.service.dto.FinalAnswerDTO;

/**
 * Service Interface for managing {@link Answer}.
 */
public interface AnswerService {


    /**
     * Save answers.
     *
     * @param answers the entity to save.
     * @return the persisted entity.
     */
    void save(FinalAnswerDTO answers);


    /**
     * Get a list of lists of the answers.
     *
     * @return the list of lists.
     */
    List<List<Answer>> getAnswers();


    /**
     * Get the size of list of lists of the answers.
     *
     * @return the size.
     */
    int getAnswersCount();




    /**
     * Get all the answers.
     *
     * @return the list of entities.
     */
    List<Answer> findAll();

    /**
     * Get the "id" answer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Answer> findOne(Long id);

    /**
     * Delete the "id" answer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


}
