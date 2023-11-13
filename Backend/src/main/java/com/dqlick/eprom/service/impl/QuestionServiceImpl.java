package com.dqlick.eprom.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.dqlick.eprom.domain.AnswerChoice;
//import com.dqlick.eprom.domain.QQuestion;
import com.dqlick.eprom.domain.Question;
import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import com.dqlick.eprom.repository.QuestionRepository;
import com.dqlick.eprom.repository.TypeQuestionRepository;
import com.dqlick.eprom.service.AnswerChoiceService;
import com.dqlick.eprom.service.LogService;
import com.dqlick.eprom.service.QuestionService;
import com.dqlick.eprom.service.dto.AnswerChoiceDTO;
import com.dqlick.eprom.service.dto.QuestionDTO;
import com.dqlick.eprom.service.dto.TypeQuestionDTO;
import com.querydsl.core.types.Predicate;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dqlick.eprom.security.SecurityUtils.getCurrentUserLogin;

/**
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);


    private final LogService logService;
    @Autowired
    private Mapper mapper;

    private final AnswerChoiceService answerChoiceService;

    @Autowired
    private  QuestionRepository questionRepository;

    @Autowired
    private  TypeQuestionRepository typeQuestionRepository;

    public QuestionServiceImpl(LogService logService ,AnswerChoiceService answerChoiceService) {
        this.logService = logService;

        this.answerChoiceService = answerChoiceService;
    }

    @Override
    public QuestionDTO save(QuestionDTO questionDTO , String ipAdress , String entity) {

        if(questionDTO.getId() != null){
            throw new IllegalStateException("Question must not have an ID");
        }

        TypeQuestionDTO tq =  questionDTO.getTypeQuestion();

        // answer choices saving
        Set<AnswerChoiceDTO> temp = new HashSet<>() ;

        questionDTO.getAnswerChoices().forEach((element) -> {
            AnswerChoiceDTO result = answerChoiceService.save(element,ipAdress);

            temp.add(result);

        });

        questionDTO.setAnswerChoices(null);

        // full question information
        Instant instant = Instant.now();
        String user ="";

        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };
        questionDTO.setCreatedBy(user);
        questionDTO.setCreatedDate(instant);
        questionDTO.setLastModifiedBy(null);
        questionDTO.setLastModifiedDate(null);
        questionDTO.setAnswerChoices(temp);
        questionDTO.setTypeQuestion(tq);


        Question question = mapper.map(questionDTO, Question.class);

        Question result = questionRepository.save(question);


        log.debug("Request to save Question : {}", questionDTO);

        //log save
        logService.save( LogAction.CREATE , entity , LogOperation.SUCCESS ,ipAdress,"Question Creation" ,user, questionDTO.getCreatedDate());

        // result.setAnswerChoices(null);

        return mapper.map(result, QuestionDTO.class);

    }

    @Override
    public QuestionDTO update(QuestionDTO questionDTO,String ipAdress , String entity) {
        log.debug("Request to save Question : {}", questionDTO);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        TypeQuestionDTO tq =  questionDTO.getTypeQuestion();

        Question oldQuestion = (this.questionRepository.findOneWithEagerRelationships(questionDTO.getId())).get();


        Set<AnswerChoiceDTO> temp = new HashSet<>() ;
        questionDTO.getAnswerChoices().forEach((element) -> {
            if( !oldQuestion.getAnswerChoices().contains(element) && element.getId()==null){
                temp.add(element);
            }else if ( !oldQuestion.getAnswerChoices().contains(element) && element.getId()!=null) {
                answerChoiceService.delete(element.getId());
            }
        });

        questionDTO.setLastModifiedBy(user);
        questionDTO.setLastModifiedDate(new Date().toInstant());
        questionDTO.setTypeQuestion(tq);

        Question question = mapper.map(questionDTO, Question.class);
        Question result = questionRepository.save(question);

        logService.save( LogAction.UPDATE , entity , LogOperation.SUCCESS ,ipAdress,"Question Update" ,user, question.getLastModifiedDate());

        return mapper.map(result, QuestionDTO.class);
    }

    @Override
    public Optional<Question> partialUpdate(Question question) {
        log.debug("Request to partially update Question : {}", question);

        return questionRepository
            .findById(question.getId())
            .map(existingQuestion -> {
                if (question.getText() != null) {
                    existingQuestion.setText(question.getText());
                }
                if (question.getCreatedBy() != null) {
                    existingQuestion.setCreatedBy(question.getCreatedBy());
                }
                if (question.getCreatedDate() != null) {
                    existingQuestion.setCreatedDate(question.getCreatedDate());
                }
                if (question.getLastModifiedBy() != null) {
                    existingQuestion.setLastModifiedBy(question.getLastModifiedBy());
                }
                if (question.getLastModifiedDate() != null) {
                    existingQuestion.setLastModifiedDate(question.getLastModifiedDate());
                }

                if (question.getLanguage() != null) {
                    existingQuestion.setLanguage(question.getLanguage());
                }

                return existingQuestion;
            })
            .map(questionRepository::save);
    }

    @Override
    @Transactional()
    public Page<Question> findAll(Pageable pageable,String ipAdress , String entity) {
        log.debug("Request to get all Questions");
        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"Questions View" ,user, date.toInstant() );

        return questionRepository.findAll(pageable);
    }

    public Page<Question> findAllWithEagerRelationships(Pageable pageable , String ipAdress , String entity) {

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"Questions View" ,user, date.toInstant() );


        pageable = PageRequest.of(pageable.getPageNumber(), 350);
        return questionRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional()
    public  Optional<Question> findOne(Long id,String ipAdress , String entity) {
        log.debug("Request to get Question : {}", id);

        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };


        logService.save( LogAction.VIEW , entity , LogOperation.SUCCESS ,ipAdress,"Question View" ,user, Instant.now() );

        Optional<Question> res = questionRepository.findOneWithEagerRelationships(id);
        System.out.println(res);

        // QuestionDTO question = mapper.map(res, QuestionDTO.class);
        return res;
    }

    @Override
    public void delete(Long id,String ipAdress , String entity) {
        log.debug("Request to delete Question : {}", id);
        String user ="";
        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };

        Date date = new Date();
        logService.save( LogAction.DELETE , entity , LogOperation.SUCCESS ,ipAdress,"Question DELETE" ,user, date.toInstant() );


        questionRepository.deleteById(id);
    }


    @Override
    public QuestionDTO searchForOne( String text,String ipAdress , String entity){

//        QQuestion qQuestion = QQuestion.question;
//        Predicate predicate = qQuestion.text.equalsIgnoreCase(text);
//        Question question = questionRepository.findOne(predicate).orElse(null);
//        if (question == null) {
//            log.debug("QuestionServiceImpl.find , cannot find question with text {}", text);
//            return null;
//        }
//        log.debug("QuestionServiceImpl.find , question name = {}", question.getText());
//
//        return mapper.map(question, QuestionDTO.class);

        return null;

    }


}
