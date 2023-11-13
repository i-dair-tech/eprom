package com.dqlick.eprom.service;


import java.time.Instant;

import com.dqlick.eprom.domain.Log;
import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import com.dqlick.eprom.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {

    private final Logger log = LoggerFactory.getLogger(LogService.class);

    private static final String ENTITY_NAME = "log";

    private final LogRepository logRepository;


    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }



    @Transactional(readOnly = true)
    public Page<Log> findAll(Pageable pageable) {
        log.debug("Request to get all logs");
        return logRepository.findAll(pageable);
    }


    public void save(LogAction action , String entity, LogOperation operation, String ipAdress, String description, String user, Instant createdDate) {
        Log log = new Log() ;
        log.setAction(action);
        log.setEntity(entity);
        log.setOperation(operation);
        log.setRemoteIp(ipAdress);
        log.setDescription(description);
        log.setCreatedBy(user);
        log.setCreatedDate(createdDate);

         logRepository.save(log);

    }


}
