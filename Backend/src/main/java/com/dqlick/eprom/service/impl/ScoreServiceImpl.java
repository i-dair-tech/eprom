package com.dqlick.eprom.service.impl;


import com.dqlick.eprom.domain.Score;
import com.dqlick.eprom.repository.ScoreRepository;
import com.dqlick.eprom.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {

    private final Logger log = LoggerFactory.getLogger(ScoreServiceImpl.class);

    private final ScoreRepository scoreRepository;

    public ScoreServiceImpl(ScoreRepository scoreRepository) {

        this.scoreRepository = scoreRepository;
    }


    @Override
    public Score save(Score score) {
        log.debug("Request to save Score : {}", score);
        return scoreRepository.save(score);
    }

    @Override
    public Optional<Score> findOne() {
        return Optional.empty();
    }


    @Override
    public Map<String, List<Map<String, Object>>> getScoreEvolutionForOwner(String owner) {

        List<Score> scores = scoreRepository.findByOwnerOrderByCreatedDateAsc(owner);

        Map<String, List<Map<String, Object>>> scoreEvolution = new HashMap<>();

        for (Score score : scores) {
            String surveyTitle = score.getSurvey().getTitle();
            List<Map<String, Object>> surveyScores = scoreEvolution.getOrDefault(surveyTitle, new ArrayList<>());

            Map<String, Object> scoreMap = new HashMap<>();
            scoreMap.put("score", score.getScore());
            scoreMap.put("createdDate", score.getCreatedDate());

            surveyScores.add(scoreMap);
            scoreEvolution.put(surveyTitle, surveyScores);
        }

        return scoreEvolution;
    }
}



