package com.dqlick.eprom.web.rest;


import com.dqlick.eprom.domain.Study;
import com.dqlick.eprom.domain.Survey;
import com.dqlick.eprom.repository.StudyRepository;
import com.dqlick.eprom.service.StudyService;
import com.dqlick.eprom.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Study}.
 */
@RestController
@RequestMapping("/api")
public class StudyResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

    private static final String ENTITY_NAME = "study";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyService studyService;

    private final StudyRepository studyRepository;

    public StudyResource(StudyService studyService, StudyRepository studyRepository) {
        this.studyService = studyService;
        this.studyRepository = studyRepository;
    }

    /**
     * {@code POST  /study} : Create a new survey.
     *
     * @param study the study to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new study, or with status {@code 400 (Bad Request)} if the study has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/study")
    public ResponseEntity<Study> createStudy(@Valid @RequestBody Study study, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Study : {}", study);
        if (study.getId() != null) {
            throw new BadRequestAlertException("A new study cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Study result = studyService.save(study, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .created(new URI("/api/study/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code PUT  /study/:id} : Updates an existing survey.
     *
     * @param id the id of the survey to save.
     * @param study the survey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated survey,
     * or with status {@code 400 (Bad Request)} if the study is not valid,
     * or with status {@code 500 (Internal Server Error)} if the study couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/study/{id}")
    public ResponseEntity<Study> updateStudy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Study study, HttpServletRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update Study : {}, {}", id, study);
        if (study.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, study.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Study result = studyService.update(study, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, study.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /study/:id} : Partial updates given fields of an existing study, field will ignore if it is null
     *
     * @param id the id of the study to save.
     * @param study the study to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated study,
     * or with status {@code 400 (Bad Request)} if the study is not valid,
     * or with status {@code 404 (Not Found)} if the study is not found,
     * or with status {@code 500 (Internal Server Error)} if the study couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/study/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Study> partialUpdateStudy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Study study
    ) throws URISyntaxException {
        log.debug("REST request to partial update Study partially : {}, {}", id, study);
        if (study.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, study.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Study> result = studyService.partialUpdate(study);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, study.getId().toString())
        );
    }

    /**
     * {@code GET  /study} : get all the studies.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studies in body.
     */
    @GetMapping("/study")
    public List<Study> getAllStudies(@RequestParam(required = false, defaultValue = "false") boolean eagerload , HttpServletRequest request) {
        log.debug("REST request to get all Studies");
        return studyService.findAll(request.getRemoteAddr() , ENTITY_NAME);
    }

    /**
     * {@code GET  /study/:id} : get the "id" study.
     *
     * @param id the id of the study to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the study, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/study/{id}")
    public ResponseEntity<Study> getStudy(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to get Study : {}", id);
        Optional<Study> study = studyService.findOne(id, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseUtil.wrapOrNotFound(study);
    }

    /**
     * {@code DELETE  /study/:id} : delete the "id" study.
     *
     * @param id the id of the study to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/study/{id}")
    public ResponseEntity<Void> deleteStudy(@PathVariable Long id , HttpServletRequest request) {
        log.debug("REST request to delete Study : {}", id);
        studyService.delete(id, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


}
