package com.dqlick.eprom.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dqlick.eprom.domain.Question;
import com.dqlick.eprom.domain.Survey;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SurveyRepositoryWithBagRelationshipsImpl implements SurveyRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Survey> fetchBagRelationships(Optional<Survey> survey) {
        return survey.map(this::fetchQuestions);
    }

    @Override
    public Page<Survey> fetchBagRelationships(Page<Survey> surveys) {
        return new PageImpl<>(fetchBagRelationships(surveys.getContent()), surveys.getPageable(), surveys.getTotalElements());
    }

    @Override
    public List<Survey> fetchBagRelationships(List<Survey> surveys) {
        return Optional.of(surveys).map(this::fetchQuestions).orElse(Collections.emptyList());
    }

    Survey fetchQuestions(Survey result) {
        return entityManager
            .createQuery("select survey from Survey survey left join fetch survey.questions where survey is :survey", Survey.class)
            .setParameter("survey", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Survey> fetchQuestions(List<Survey> surveys) {
        return entityManager
            .createQuery(
                "select distinct survey from Survey survey left join fetch survey.questions where survey in :surveys",
                Survey.class
            )
            .setParameter("surveys", surveys)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }


}
