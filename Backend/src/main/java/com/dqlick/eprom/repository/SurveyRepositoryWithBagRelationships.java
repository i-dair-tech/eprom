package com.dqlick.eprom.repository;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.Survey;
import org.springframework.data.domain.Page;

public interface SurveyRepositoryWithBagRelationships {
    Optional<Survey> fetchBagRelationships(Optional<Survey> survey);

    abstract List<Survey> fetchBagRelationships(List<Survey> surveys);

    Page<Survey> fetchBagRelationships(Page<Survey> surveys);
}
