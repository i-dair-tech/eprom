package com.dqlick.eprom.repository;

import com.dqlick.eprom.domain.TypeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeQuestionRepository extends JpaRepository<TypeQuestion, Long> {}
