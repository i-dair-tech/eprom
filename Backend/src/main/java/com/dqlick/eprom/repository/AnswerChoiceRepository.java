package com.dqlick.eprom.repository;

import com.dqlick.eprom.domain.AnswerChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AnswerChoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerChoiceRepository extends JpaRepository<AnswerChoice, Long> {}
