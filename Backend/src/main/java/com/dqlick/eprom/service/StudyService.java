package com.dqlick.eprom.service;

import com.dqlick.eprom.domain.Study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudyService {


    /**
     * Save a study.
     *
     * @param study the entity to save.
     * @return the persisted entity.
     */
    Study save(Study study, String ipAdress , String entity);


    Map<String, Long> countInvitationsBySurvey(Long studyId);


    Map<String, Map<String, Map<LocalDate, Integer>>> getScoresByOwnersAndSurveysInStudy(Long studyId);


    /**
     * Updates a study.
     *
     * @param study the entity to update.
     * @return the persisted entity.
     */
    Study update(Study study , String ipAdress , String entity);

    /**
     * Partially updates a study.
     *
     * @param study the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Study> partialUpdate(Study study);

    /**
     * Get all the studies.
     *
     * @return the list of entities.
     */
    List<Study> findAll(String ipAdress , String entity);


    /**
     * Get the "id" study.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Study> findOne(Long id, String ipAdress , String entity);

    /**
     * Delete the "id" study.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, String ipAdress , String entity);






}
