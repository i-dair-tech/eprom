package com.dqlick.eprom.repository;

import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Survey entity.
 */
@Repository
public interface SurveyRepository extends SurveyRepositoryWithBagRelationships, JpaRepository<Survey, Long> {
    default Optional<Survey> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Survey> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Survey> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query(value ="select * from eprom_survey where id IN (select survey_id from eprom_invitation where email LIKE :email );", nativeQuery = true)
    List<Survey> findAllByEmail(@Param("email") String email);


    @Query(value ="SELECT COUNT(id) FROM eprom_survey;", nativeQuery = true)
    int getSurveyCreatedNumber();

}
