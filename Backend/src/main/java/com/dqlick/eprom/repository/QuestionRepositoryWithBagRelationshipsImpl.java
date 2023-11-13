package com.dqlick.eprom.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dqlick.eprom.domain.Question;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class QuestionRepositoryWithBagRelationshipsImpl implements QuestionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Question> fetchBagRelationships(Optional<Question> question) {
        return question.map(this::fetchAnswerChoices).map(this::fetchTypeQuestion);
    }

    //initial one
//    @Override
//    public Optional<Question> fetchBagRelationships(Optional<Question> question) {
//        return question.map(this::fetchAnswerChoices);
//    }

    @Override
    public Page<Question> fetchBagRelationships(Page<Question> questions) {
        return new PageImpl<>(fetchBagRelationships(questions.getContent()), questions.getPageable(), questions.getTotalElements());
    }

    @Override
    public List<Question> fetchBagRelationships(List<Question> questions) {
        return Optional.of(questions).map(this::fetchAnswerChoices).map(this::fetchQuestionsTypes).orElse(Collections.emptyList());
    }

    Question fetchAnswerChoices(Question result) {
        return entityManager
            .createQuery(
                "select question from Question question left join fetch question.answerChoices where question is :question",
                Question.class
            )
            .setParameter("question", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    Question fetchTypeQuestion(Question result) {
        return entityManager
            .createQuery(
                "select question from Question question left join fetch question.typeQuestion where question is :question",
                Question.class
            )
            .setParameter("question", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }


    List<Question> fetchAnswerChoices(List<Question> questions) {
        return entityManager
            .createQuery(
                "select distinct question from Question question left join fetch question.answerChoices where question in :questions",
                Question.class
            )
            .setParameter("questions", questions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
    List<Question> fetchQuestionsTypes(List<Question> questions) {
        return entityManager
            .createQuery(
                "select distinct question from Question question left join fetch question.typeQuestion where question in :questions",
                Question.class
            )
            .setParameter("questions", questions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
