package com.dqlick.eprom.repository;

import com.dqlick.eprom.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ScoreRepository extends JpaRepository<Score, Long>, CrudRepository<Score, Long> {

    List<Score> findByOwnerOrderByCreatedDateAsc(String owner);

    @Query(nativeQuery = true,
        value = "SELECT s.owner AS owner, sv.title AS surveyTitle, s.created_date AS creationDate, s.score AS score " +
            "FROM eprom_score s " +
            "INNER JOIN eprom_survey sv ON s.survey_id = sv.id " +
            "WHERE sv.id IN :surveyIds")
    List<Object[]> findScoresByOwnersAndSurveysWithCreationDate(@Param("surveyIds") Set<Long> surveyIds);


}
