package com.dqlick.eprom.repository;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.Question;
import org.springframework.data.domain.Page;

public interface QuestionRepositoryWithBagRelationships {
    Optional<Question> fetchBagRelationships(Optional<Question> question);

    List<Question> fetchBagRelationships(List<Question> questions);

    Page<Question> fetchBagRelationships(Page<Question> questions);
}
