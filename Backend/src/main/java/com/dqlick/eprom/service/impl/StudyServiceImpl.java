package com.dqlick.eprom.service.impl;


import com.dqlick.eprom.domain.Study;
import com.dqlick.eprom.domain.Survey;
import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import com.dqlick.eprom.repository.InvitationRepository;
import com.dqlick.eprom.repository.ScoreRepository;
import com.dqlick.eprom.repository.StudyRepository;
import com.dqlick.eprom.service.LogService;
import com.dqlick.eprom.service.StudyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.dqlick.eprom.security.SecurityUtils.getCurrentUserLogin;

@Service
@Transactional
public class StudyServiceImpl implements StudyService {

    private final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    private final LogService logService;
    private final StudyRepository studyRepository;
    private final InvitationRepository invitationRepository;
    private final ScoreRepository scoreRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public StudyServiceImpl(LogService logService, StudyRepository studyRepository, InvitationRepository invitationRepository, ScoreRepository scoreRepository) {
        this.logService = logService;
        this.studyRepository = studyRepository;
        this.invitationRepository = invitationRepository;
        this.scoreRepository = scoreRepository;
    }


    @Override
    public Study save(Study study, String ipAdress, String entity) {
        log.debug("Request to save Study : {}", study);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };
        study.setCreatedBy(user);
        study.setCreatedDate(new Date().toInstant());
        study.setIsArchived(false);
        study.setArchivedDate(null);
        study.setLastModifiedBy(null);
        study.setLastModifiedDate(null);

        Study result =  studyRepository.save(study);

        logService.save( LogAction.CREATE , entity , LogOperation.SUCCESS ,ipAdress,"Study Creation" ,user, study.getCreatedDate());

        return result;
    }



    @Override
    public Map<String, Long> countInvitationsBySurvey(Long studyId) {
        Set<Survey> surveys = studyRepository.findById(studyId).get().getSurveys();
        Map<String, Long> invitationsPerSurvey = new HashMap<>();

        for (Survey survey : surveys) {
            Long invitations = invitationRepository.countBySurveyId(survey.getId());
            invitationsPerSurvey.put(survey.getTitle(), invitations);
        }
        return invitationsPerSurvey;
    }



    @Override
    public Map<String, Map<String, Map<LocalDate, Integer>>> getScoresByOwnersAndSurveysInStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new IllegalArgumentException("Study not found"));

        Set<Long> surveyIds = study.getSurveys().stream()
            .map(Survey::getId)
            .collect(Collectors.toSet());

        List<Object[]> scoresByOwnersAndSurveys = scoreRepository.findScoresByOwnersAndSurveysWithCreationDate(surveyIds);

        Map<String, Map<String, Map<LocalDate, Integer>>> scoresByOwnersAndSurveysAndDate = new HashMap<>();

        for (Object[] result : scoresByOwnersAndSurveys) {
            String owner = (String) result[0];
            String surveyTitle = (String) result[1];
            LocalDate date = ((Date) result[2]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            int score = ((Number) result[3]).intValue();

            Map<String, Map<LocalDate, Integer>> scoresBySurveyAndDate = scoresByOwnersAndSurveysAndDate.computeIfAbsent(owner, k -> new HashMap<>());
            Map<LocalDate, Integer> scoresByDate = scoresBySurveyAndDate.computeIfAbsent(surveyTitle, k -> new HashMap<>());

            scoresByDate.put(date, score);
        }

        return scoresByOwnersAndSurveysAndDate;
    }






    @Override
    public Study update(Study study, String ipAdress, String entity) {
        log.debug("Request to save Study : {}", study);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        study.setLastModifiedBy(user);
        study.setLastModifiedDate(new Date().toInstant());

        logService.save( LogAction.UPDATE , entity , LogOperation.SUCCESS ,ipAdress,"Study Update" ,user, study.getLastModifiedDate());

        return studyRepository.save(study);
    }


    @Override
    public Optional<Study> partialUpdate(Study study) {
        log.debug("Request to partially update Survey : {}", study);

        return studyRepository
            .findById(study.getId())
            .map(existingSurvey -> {
                if (study.getTitle() != null) {
                    existingSurvey.setTitle(study.getTitle());
                }
                if (study.getCreatedBy() != null) {
                    existingSurvey.setCreatedBy(study.getCreatedBy());
                }
                if (study.getCreatedDate() != null) {
                    existingSurvey.setCreatedDate(study.getCreatedDate());
                }
                if (study.getLastModifiedBy() != null) {
                    existingSurvey.setLastModifiedBy(study.getLastModifiedBy());
                }
                if (study.getLastModifiedDate() != null) {
                    existingSurvey.setLastModifiedDate(study.getLastModifiedDate());
                }
                if (study.getIsArchived() != null) {
                    existingSurvey.setIsArchived(study.getIsArchived());
                }
                if (study.getArchivedDate() != null) {
                    existingSurvey.setArchivedDate(study.getArchivedDate());
                }

                return existingSurvey;
            })
            .map(studyRepository::save);
    }

    @Override
    @Transactional()
    public List<Study> findAll(String ipAdress, String entity) {
        log.debug("Request to get all Studies");
        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"Studies View" ,user, date.toInstant() );


        return studyRepository.findAll();

    }



    @Override
    @Transactional()
    public Optional<Study> findOne(Long id, String ipAdress, String entity) {
        log.debug("Request to get Study : {}", id);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"Study View" ,user, date.toInstant() );


        return studyRepository.findById(id);
    }



    @Override
    public void delete(Long id, String ipAdress, String entity) {

        log.debug("Request to delete Study : {}", id);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.DELETE , entity , LogOperation.SUCCESS ,ipAdress,"Study DELETE" ,user, date.toInstant() );

        studyRepository.deleteById(id);
    }
}
