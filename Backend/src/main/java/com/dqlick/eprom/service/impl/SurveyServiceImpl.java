package com.dqlick.eprom.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.Survey;
import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import com.dqlick.eprom.repository.SurveyRepository;
import com.dqlick.eprom.service.LogService;
import com.dqlick.eprom.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dqlick.eprom.security.SecurityUtils.getCurrentUserLogin;

/**
 * Service Implementation for managing {@link Survey}.
 */
@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    private final LogService logService;
    private final SurveyRepository surveyRepository;


    public SurveyServiceImpl(LogService logService,SurveyRepository surveyRepository) {
        this.logService = logService;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public Survey save(Survey survey, String ipAdress , String entity) {
        log.debug("Request to save Survey : {}", survey);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };
        survey.setCreatedBy(user);
        survey.setCreatedDate(new Date().toInstant());
        survey.setIsArchived(false);
        survey.setArchivedDate(null);
        survey.setLastModifiedBy(null);
        survey.setLastModifiedDate(null);

        Survey result =  surveyRepository.save(survey);


        logService.save( LogAction.CREATE , entity , LogOperation.SUCCESS ,ipAdress,"Survey Creation" ,user, survey.getCreatedDate());

        return result;
    }

    @Override
    public Survey update(Survey survey,String ipAdress , String entity) {
        log.debug("Request to save Survey : {}", survey);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        survey.setLastModifiedBy(user);
        survey.setLastModifiedDate(new Date().toInstant());

        logService.save( LogAction.UPDATE , entity , LogOperation.SUCCESS ,ipAdress,"Survey Update" ,user, survey.getLastModifiedDate());


        return surveyRepository.save(survey);
    }

    @Override
    public Optional<Survey> partialUpdate(Survey survey) {
        log.debug("Request to partially update Survey : {}", survey);

        return surveyRepository
            .findById(survey.getId())
            .map(existingSurvey -> {
                if (survey.getTitle() != null) {
                    existingSurvey.setTitle(survey.getTitle());
                }
                if (survey.getTopic() != null) {
                    existingSurvey.setTopic(survey.getTopic());
                }
                if (survey.getDescription() != null) {
                    existingSurvey.setDescription(survey.getDescription());
                }

                if (survey.getCreatedBy() != null) {
                    existingSurvey.setCreatedBy(survey.getCreatedBy());
                }
                if (survey.getCreatedDate() != null) {
                    existingSurvey.setCreatedDate(survey.getCreatedDate());
                }
                if (survey.getLastModifiedBy() != null) {
                    existingSurvey.setLastModifiedBy(survey.getLastModifiedBy());
                }
                if (survey.getLastModifiedDate() != null) {
                    existingSurvey.setLastModifiedDate(survey.getLastModifiedDate());
                }
                if (survey.getIsArchived() != null) {
                    existingSurvey.setIsArchived(survey.getIsArchived());
                }
                if (survey.getArchivedDate() != null) {
                    existingSurvey.setArchivedDate(survey.getArchivedDate());
                }

                return existingSurvey;
            })
            .map(surveyRepository::save);
    }

    @Override
    @Transactional()
    public List<Survey> findAll(String ipAdress , String entity) {
        log.debug("Request to get all Surveys");
        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"Surveys View" ,user, date.toInstant() );


        return surveyRepository.findAllWithEagerRelationships();
    }

    public Page<Survey> findAllWithEagerRelationships(Pageable pageable) {
        return surveyRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional()
    public Optional<Survey> findOne(Long id,String ipAdress , String entity) {
        log.debug("Request to get Survey : {}", id);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"Survey View" ,user, date.toInstant() );


        return surveyRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id ,String ipAdress , String entity) {
        log.debug("Request to delete Survey : {}", id);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.DELETE , entity , LogOperation.SUCCESS ,ipAdress,"Survey DELETE" ,user, date.toInstant() );

        surveyRepository.deleteById(id);
    }

    /**
     * @param email
     * @param ipAdress
     * @param entity
     * @return
     */
    @Override
    public List<Survey> findAllByEmail(String email, String ipAdress, String entity) {

        List<Survey> s = surveyRepository.findAllByEmail(email);
        return surveyRepository.fetchBagRelationships(s);
    }
}
