package com.dqlick.eprom.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dqlick.eprom.domain.AnswerChoice;
import com.dqlick.eprom.repository.AnswerChoiceRepository;
import com.dqlick.eprom.service.AnswerChoiceService;
import com.dqlick.eprom.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link AnswerChoice}.
 */
@RestController
@RequestMapping("/api")
public class AnswerChoiceResource {

    private final Logger log = LoggerFactory.getLogger(AnswerChoiceResource.class);

    private static final String ENTITY_NAME = "answerChoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerChoiceService answerChoiceService;

    private final AnswerChoiceRepository answerChoiceRepository;

    public AnswerChoiceResource(AnswerChoiceService answerChoiceService, AnswerChoiceRepository answerChoiceRepository) {
        this.answerChoiceService = answerChoiceService;
        this.answerChoiceRepository = answerChoiceRepository;
    }




    /**
     * {@code PUT  /answer-choices/:id} : Updates an existing answerChoice.
     *
     * @param id the id of the answerChoice to save.
     * @param answerChoice the answerChoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerChoice,
     * or with status {@code 400 (Bad Request)} if the answerChoice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerChoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/answer-choices/{id}")
    public ResponseEntity<AnswerChoice> updateAnswerChoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnswerChoice answerChoice
    ) throws URISyntaxException {
        log.debug("REST request to update AnswerChoice : {}, {}", id, answerChoice);
        if (answerChoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerChoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerChoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnswerChoice result = answerChoiceService.update(answerChoice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerChoice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /answer-choices/:id} : Partial updates given fields of an existing answerChoice, field will ignore if it is null
     *
     * @param id the id of the answerChoice to save.
     * @param answerChoice the answerChoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerChoice,
     * or with status {@code 400 (Bad Request)} if the answerChoice is not valid,
     * or with status {@code 404 (Not Found)} if the answerChoice is not found,
     * or with status {@code 500 (Internal Server Error)} if the answerChoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/answer-choices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnswerChoice> partialUpdateAnswerChoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnswerChoice answerChoice
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnswerChoice partially : {}, {}", id, answerChoice);
        if (answerChoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerChoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerChoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnswerChoice> result = answerChoiceService.partialUpdate(answerChoice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerChoice.getId().toString())
        );
    }

    /**
     * {@code GET  /answer-choices} : get all the answerChoices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of answerChoices in body.
     */
    @GetMapping("/answer-choices")
    public List<AnswerChoice> getAllAnswerChoices() {
        log.debug("REST request to get all AnswerChoices");
        return answerChoiceService.findAll();
    }

    /**
     * {@code GET  /answer-choices/:id} : get the "id" answerChoice.
     *
     * @param id the id of the answerChoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answerChoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/answer-choices/{id}")
    public ResponseEntity<AnswerChoice> getAnswerChoice(@PathVariable Long id) {
        log.debug("REST request to get AnswerChoice : {}", id);
        Optional<AnswerChoice> answerChoice = answerChoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerChoice);
    }

    /**
     * {@code DELETE  /answer-choices/:id} : delete the "id" answerChoice.
     *
     * @param id the id of the answerChoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/answer-choices/{id}")
    public ResponseEntity<Void> deleteAnswerChoice(@PathVariable Long id) {
        log.debug("REST request to delete AnswerChoice : {}", id);
        answerChoiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
