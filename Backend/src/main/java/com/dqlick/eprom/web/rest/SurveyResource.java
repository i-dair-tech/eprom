package com.dqlick.eprom.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dqlick.eprom.domain.Survey;
import com.dqlick.eprom.repository.SurveyRepository;
import com.dqlick.eprom.service.SurveyService;
import com.dqlick.eprom.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Survey}.
 */
@RestController
@RequestMapping("/api")
public class SurveyResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

    private static final String ENTITY_NAME = "survey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveyService surveyService;

    private final SurveyRepository surveyRepository;

    public SurveyResource(SurveyService surveyService, SurveyRepository surveyRepository) {
        this.surveyService = surveyService;
        this.surveyRepository = surveyRepository;
    }

    /**
     * {@code POST  /surveys} : Create a new survey.
     *
     * @param survey the survey to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new survey, or with status {@code 400 (Bad Request)} if the survey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/surveys")
    public ResponseEntity<Survey> createSurvey(@Valid @RequestBody Survey survey, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Survey : {}", survey);
        if (survey.getId() != null) {
            throw new BadRequestAlertException("A new survey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Survey result = surveyService.save(survey, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .created(new URI("/api/surveys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /surveys/:id} : Updates an existing survey.
     *
     * @param id the id of the survey to save.
     * @param survey the survey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated survey,
     * or with status {@code 400 (Bad Request)} if the survey is not valid,
     * or with status {@code 500 (Internal Server Error)} if the survey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/surveys/{id}")
    public ResponseEntity<Survey> updateSurvey(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Survey survey, HttpServletRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update Survey : {}, {}", id, survey);
        if (survey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, survey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Survey result = surveyService.update(survey, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, survey.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /surveys/:id} : Partial updates given fields of an existing survey, field will ignore if it is null
     *
     * @param id the id of the survey to save.
     * @param survey the survey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated survey,
     * or with status {@code 400 (Bad Request)} if the survey is not valid,
     * or with status {@code 404 (Not Found)} if the survey is not found,
     * or with status {@code 500 (Internal Server Error)} if the survey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/surveys/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Survey> partialUpdateSurvey(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Survey survey
    ) throws URISyntaxException {
        log.debug("REST request to partial update Survey partially : {}, {}", id, survey);
        if (survey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, survey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Survey> result = surveyService.partialUpdate(survey);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, survey.getId().toString())
        );
    }

    /**
     * {@code GET  /surveys} : get all the surveys.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surveys in body.
     */
    @GetMapping("/surveys")
    public List<Survey> getAllSurveys(@RequestParam(required = false, defaultValue = "false") boolean eagerload , HttpServletRequest request) {
        log.debug("REST request to get all Surveys");
        return surveyService.findAll(request.getRemoteAddr() , ENTITY_NAME);
    }

    /**
     * {@code GET  /surveys/:id} : get the "id" survey.
     *
     * @param id the id of the survey to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the survey, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/surveys/{id}")
    public ResponseEntity<Survey> getSurvey(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to get Survey : {}", id);
        Optional<Survey> survey = surveyService.findOne(id, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseUtil.wrapOrNotFound(survey);
    }

    /**
     * {@code DELETE  /surveys/:id} : delete the "id" survey.
     *
     * @param id the id of the survey to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @RequestMapping(value = "/surveys/{id}",method = { RequestMethod.DELETE })
    @ResponseBody
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id , HttpServletRequest request) {
        log.debug("REST request to delete Survey : {}", id);
        surveyService.delete(id , request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }






    /**
     * {@code GET  /surveys/:email} : get all the surveys by email.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surveys in body.
     */
    @GetMapping("/surveys-email/{email}")
    public List<Survey> getAllSurveysByEmail(@PathVariable String email, HttpServletRequest request) {
        log.debug("REST request to get all Surveys by email");
        return surveyService.findAllByEmail(email,request.getRemoteAddr() , ENTITY_NAME);
    }
}
