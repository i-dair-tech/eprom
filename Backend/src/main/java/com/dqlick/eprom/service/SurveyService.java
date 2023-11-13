package com.dqlick.eprom.service;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Survey}.
 */
public interface SurveyService {
    /**
     * Save a survey.
     *
     * @param survey the entity to save.
     * @return the persisted entity.
     */
    Survey save(Survey survey, String ipAdress , String entity);

    /**
     * Updates a survey.
     *
     * @param survey the entity to update.
     * @return the persisted entity.
     */
    Survey update(Survey survey , String ipAdress , String entity);

    /**
     * Partially updates a survey.
     *
     * @param survey the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Survey> partialUpdate(Survey survey);

    /**
     * Get all the surveys.
     *
     * @return the list of entities.
     */
    List<Survey> findAll( String ipAdress , String entity);

    /**
     * Get all the surveys with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Survey> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" survey.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Survey> findOne(Long id, String ipAdress , String entity);

    /**
     * Delete the "id" survey.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, String ipAdress , String entity);



    /**
     * Get all the surveys by Email.
     *
     * @return the list of entities.
     */
    List<Survey> findAllByEmail( String email , String ipAdress , String entity);

}
