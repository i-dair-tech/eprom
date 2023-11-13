package com.dqlick.eprom.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.TypeQuestion;
import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import com.dqlick.eprom.repository.TypeQuestionRepository;
import com.dqlick.eprom.service.LogService;
import com.dqlick.eprom.service.TypeQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dqlick.eprom.security.SecurityUtils.getCurrentUserLogin;

/**
 * Service Implementation for managing {@link TypeQuestion}.
 */
@Service
@Transactional
public class TypeQuestionServiceImpl implements TypeQuestionService {

    private final Logger log = LoggerFactory.getLogger(TypeQuestionServiceImpl.class);

    private final TypeQuestionRepository typeQuestionRepository;

    private final LogService logService;

    public TypeQuestionServiceImpl(TypeQuestionRepository typeQuestionRepository, LogService logService) {
        this.typeQuestionRepository = typeQuestionRepository;
        this.logService = logService;
    }

    @Override
    public TypeQuestion save(TypeQuestion typeQuestion,  String ipAdress , String entity) {
        log.debug("Request to save TypeQuestion : {}", typeQuestion);

        Instant instant = Instant.now();

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };
        typeQuestion.setCreatedBy(user);
        typeQuestion.setCreatedDate(instant);
        typeQuestion.setIsArchived(false);
        typeQuestion.setArchivedDate(null);
        typeQuestion.setLastModifiedBy(null);
        typeQuestion.setLastModifiedDate(null);

        logService.save( LogAction.CREATE , entity , LogOperation.SUCCESS ,ipAdress,"Type Question Creation" ,user, typeQuestion.getCreatedDate());

        return typeQuestionRepository.save(typeQuestion);
    }

    @Override
    public TypeQuestion update(TypeQuestion typeQuestion,String ipAdress , String entity) {
        log.debug("Request to save TypeQuestion : {}", typeQuestion);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        typeQuestion.setLastModifiedBy(user);
        typeQuestion.setLastModifiedDate(new Date().toInstant());

        logService.save( LogAction.UPDATE , entity , LogOperation.SUCCESS ,ipAdress,"Question Update" ,user, typeQuestion.getLastModifiedDate());

        return typeQuestionRepository.save(typeQuestion);
    }

    @Override
    public Optional<TypeQuestion> partialUpdate(TypeQuestion typeQuestion) {
        log.debug("Request to partially update TypeQuestion : {}", typeQuestion);

        return typeQuestionRepository
            .findById(typeQuestion.getId())
            .map(existingTypeQuestion -> {
                if (typeQuestion.getText() != null) {
                    existingTypeQuestion.setText(typeQuestion.getText());
                }
                if (typeQuestion.getCreatedBy() != null) {
                    existingTypeQuestion.setCreatedBy(typeQuestion.getCreatedBy());
                }
                if (typeQuestion.getCreatedDate() != null) {
                    existingTypeQuestion.setCreatedDate(typeQuestion.getCreatedDate());
                }
                if (typeQuestion.getLastModifiedBy() != null) {
                    existingTypeQuestion.setLastModifiedBy(typeQuestion.getLastModifiedBy());
                }
                if (typeQuestion.getLastModifiedDate() != null) {
                    existingTypeQuestion.setLastModifiedDate(typeQuestion.getLastModifiedDate());
                }
                if (typeQuestion.getIsArchived() != null) {
                    existingTypeQuestion.setIsArchived(typeQuestion.getIsArchived());
                }
                if (typeQuestion.getArchivedDate() != null) {
                    existingTypeQuestion.setArchivedDate(typeQuestion.getArchivedDate());
                }

                return existingTypeQuestion;
            })
            .map(typeQuestionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeQuestion> findAll(String ipAdress , String entity) {
        log.debug("Request to get all TypeQuestions");

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"All TypeQuestions View" ,user, date.toInstant() );

        return typeQuestionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeQuestion> findOne(Long id,String ipAdress , String entity) {
        log.debug("Request to get TypeQuestion : {}", id);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"TypeQuestion View" ,user, date.toInstant() );


        return typeQuestionRepository.findById(id);
    }

    @Override
    public void delete(Long id,String ipAdress , String entity) {
        log.debug("Request to delete TypeQuestion : {}", id);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.DELETE , entity , LogOperation.SUCCESS ,ipAdress,"TypeQuestion DELETE" ,user, date.toInstant() );

                    try{
                        typeQuestionRepository.deleteById(id);
                    }catch(Exception e){
                        System.out.println("YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                    }

    }
}
