package com.dqlick.eprom.web.rest;


import com.dqlick.eprom.domain.Log;

import com.dqlick.eprom.repository.LogRepository;
import com.dqlick.eprom.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LogsResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "log";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LogService logService;

    private final LogRepository logRepository;


    public LogsResource(LogService logService, LogRepository logRepository) {
        this.logService = logService;
        this.logRepository = logRepository;
    }



    /**
     * {@code GET  /logs} : get all the logs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of logs in body.
     */
    @GetMapping("/logs")
    public List<Log> getAllLogs(@RequestParam(required = false, defaultValue = "false") boolean eagerload , HttpServletRequest request) {
        log.debug("REST request to get all Logs");
        return logRepository.findAllByOrderByDateAsc() ;
    }





}
