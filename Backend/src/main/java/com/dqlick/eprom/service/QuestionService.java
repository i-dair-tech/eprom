package com.dqlick.eprom.service;

import java.util.Optional;

import com.dqlick.eprom.domain.Question;
import com.dqlick.eprom.service.dto.QuestionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Question}.
 */
public interface QuestionService {
    /**
     * Save a question.
     *
     * @param question the entity to save.
     * @return the persisted entity.
     */
    QuestionDTO save(QuestionDTO question , String ipAdress , String entity);

    /**
     * Updates a question.
     *
     * @param questionDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionDTO update(QuestionDTO questionDTO  ,String ipAdress , String entity);

    /**
     * Partially updates a question.
     *
     * @param question the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Question> partialUpdate(Question question);

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Question> findAll(Pageable pageable,String ipAdress , String entity);

    /**
     * Get all the questions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Question> findAllWithEagerRelationships(Pageable pageable, String ipAdress , String entity);

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Question> findOne(Long id , String ipAdress , String entity);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    void delete( Long id,String ipAdress , String entity);


    /**
     * find the "text" question.
     *
     * @param text the text of the entity.
     */
    QuestionDTO searchForOne( String text,String ipAdress , String entity);
}
