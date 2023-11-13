package com.dqlick.eprom.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import com.dqlick.eprom.domain.AnswerChoice;
import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import com.dqlick.eprom.repository.AnswerChoiceRepository;
import com.dqlick.eprom.service.AnswerChoiceService;
import com.dqlick.eprom.service.LogService;
import com.dqlick.eprom.service.dto.AnswerChoiceDTO;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dqlick.eprom.security.SecurityUtils.getCurrentUserLogin;

/**
 * Service Implementation for managing {@link AnswerChoice}.
 */
@Service
@Transactional
public class AnswerChoiceServiceImpl implements AnswerChoiceService {

    private final Logger log = LoggerFactory.getLogger(AnswerChoiceServiceImpl.class);

    private final LogService logService;

    @Autowired
    private Mapper mapper;

    private final AnswerChoiceRepository answerChoiceRepository;

    public AnswerChoiceServiceImpl(AnswerChoiceRepository answerChoiceRepository,LogService logService) {
        this.answerChoiceRepository = answerChoiceRepository;
        this.logService=logService;
    }

    @Override
    @Transactional
    public AnswerChoiceDTO save(AnswerChoiceDTO answerChoiceDTO, String ipAdress) {
        log.debug("Request to save AnswerChoice : {}", answerChoiceDTO);

        System.out.println(answerChoiceDTO.getText());
        String user ="";

        if (getCurrentUserLogin().isPresent()){
            user = getCurrentUserLogin().get();
        };
        Instant instant = Instant.now();

        answerChoiceDTO.setCreatedBy(user);
        answerChoiceDTO.setCreatedDate(answerChoiceDTO.getCreatedDate());
        answerChoiceDTO.setIsArchived(false);
        answerChoiceDTO.setArchivedDate(null);
        answerChoiceDTO.setLastModifiedBy(null);
        answerChoiceDTO.setLastModifiedDate(null);
        logService.save( LogAction.CREATE , "AnswerChoice" , LogOperation.SUCCESS ,ipAdress,"Question Creation" ,user, answerChoiceDTO.getCreatedDate());

        AnswerChoice answerChoice = mapper.map(answerChoiceDTO, AnswerChoice.class);
        System.out.println(answerChoice);


        return mapper.map(answerChoiceRepository.save(answerChoice), AnswerChoiceDTO.class);
    }

    @Override
    public AnswerChoice update(AnswerChoice answerChoice) {
        log.debug("Request to save AnswerChoice : {}", answerChoice);
        return answerChoiceRepository.save(answerChoice);
    }

    @Override
    public Optional<AnswerChoice> partialUpdate(AnswerChoice answerChoice) {
        log.debug("Request to partially update AnswerChoice : {}", answerChoice);

        return answerChoiceRepository
            .findById(answerChoice.getId())
            .map(existingAnswerChoice -> {
                if (answerChoice.getText() != null) {
                    existingAnswerChoice.setText(answerChoice.getText());
                }
                if (answerChoice.getCreatedBy() != null) {
                    existingAnswerChoice.setCreatedBy(answerChoice.getCreatedBy());
                }
                if (answerChoice.getCreatedDate() != null) {
                    existingAnswerChoice.setCreatedDate(answerChoice.getCreatedDate());
                }
                if (answerChoice.getLastModifiedBy() != null) {
                    existingAnswerChoice.setLastModifiedBy(answerChoice.getLastModifiedBy());
                }
                if (answerChoice.getLastModifiedDate() != null) {
                    existingAnswerChoice.setLastModifiedDate(answerChoice.getLastModifiedDate());
                }
                if (answerChoice.getIsArchived() != null) {
                    existingAnswerChoice.setIsArchived(answerChoice.getIsArchived());
                }
                if (answerChoice.getArchivedDate() != null) {
                    existingAnswerChoice.setArchivedDate(answerChoice.getArchivedDate());
                }

                return existingAnswerChoice;
            })
            .map(answerChoiceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerChoice> findAll() {
        log.debug("Request to get all AnswerChoices");
        return answerChoiceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnswerChoice> findOne(Long id) {
        log.debug("Request to get AnswerChoice : {}", id);
        return answerChoiceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnswerChoice : {}", id);
        answerChoiceRepository.deleteById(id);
    }
}
