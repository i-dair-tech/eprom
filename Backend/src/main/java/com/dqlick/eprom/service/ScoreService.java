package com.dqlick.eprom.service;

import com.dqlick.eprom.domain.Score;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ScoreService {


        /**
         * Save a score.
         *
         * @param score the entity to save.
         * @return the persisted entity.
         */
        Score save(Score score);



        /**
         * Get the "id" Score.
         *
         * @return the entity.
         */
        Optional<Score> findOne();


    Map<String, List<Map<String, Object>>>  getScoreEvolutionForOwner(String owner);
}
