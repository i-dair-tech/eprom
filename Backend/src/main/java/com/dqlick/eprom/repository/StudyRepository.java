package com.dqlick.eprom.repository;


import com.dqlick.eprom.domain.Study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long>, CrudRepository<Study, Long> {


    Optional<Study> findOneByTitle(String title);

    @Query(value ="SELECT COUNT(id) FROM eprom_study;", nativeQuery = true)
    int getStudyCreatedNumber();

}
