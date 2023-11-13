package com.dqlick.eprom.repository;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Question entity.
 */

@Repository
public interface QuestionRepository extends QuestionRepositoryWithBagRelationships, JpaRepository<Question, Long>, CrudRepository<Question, Long>

{
    default Optional<Question> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Question> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Question> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query(value ="SELECT COUNT(id) FROM eprom_question;", nativeQuery = true)
    int getQuestionCreatedNumber();
}
