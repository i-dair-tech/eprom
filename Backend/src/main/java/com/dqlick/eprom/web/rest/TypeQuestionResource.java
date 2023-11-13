package com.dqlick.eprom.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dqlick.eprom.domain.TypeQuestion;
import com.dqlick.eprom.repository.TypeQuestionRepository;
import com.dqlick.eprom.service.TypeQuestionService;
import com.dqlick.eprom.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link TypeQuestion}.
 */
@RestController
@RequestMapping("/api")
public class TypeQuestionResource {

    private final Logger log = LoggerFactory.getLogger(TypeQuestionResource.class);

    private static final String ENTITY_NAME = "typeQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeQuestionService typeQuestionService;

    private final TypeQuestionRepository typeQuestionRepository;

    public TypeQuestionResource(TypeQuestionService typeQuestionService, TypeQuestionRepository typeQuestionRepository) {
        this.typeQuestionService = typeQuestionService;
        this.typeQuestionRepository = typeQuestionRepository;
    }

    /**
     * {@code POST  /type-questions} : Create a new typeQuestion.
     *
     * @param typeQuestion the typeQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeQuestion, or with status {@code 400 (Bad Request)} if the typeQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-questions")
    public ResponseEntity<TypeQuestion> createTypeQuestion(@Valid @RequestBody TypeQuestion typeQuestion, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save TypeQuestion : {}", typeQuestion);
        if (typeQuestion.getId() != null) {
            throw new BadRequestAlertException("A new typeQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeQuestion result = typeQuestionService.save(typeQuestion, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .created(new URI("/api/type-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-questions/:id} : Updates an existing typeQuestion.
     *
     * @param id the id of the typeQuestion to save.
     * @param typeQuestion the typeQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeQuestion,
     * or with status {@code 400 (Bad Request)} if the typeQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-questions/{id}")
    public ResponseEntity<TypeQuestion> updateTypeQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeQuestion typeQuestion, HttpServletRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update TypeQuestion : {}, {}", id, typeQuestion);
        if (typeQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeQuestion result = typeQuestionService.update(typeQuestion, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeQuestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-questions/:id} : Partial updates given fields of an existing typeQuestion, field will ignore if it is null
     *
     * @param id the id of the typeQuestion to save.
     * @param typeQuestion the typeQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeQuestion,
     * or with status {@code 400 (Bad Request)} if the typeQuestion is not valid,
     * or with status {@code 404 (Not Found)} if the typeQuestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeQuestion> partialUpdateTypeQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeQuestion typeQuestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeQuestion partially : {}, {}", id, typeQuestion);
        if (typeQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeQuestion> result = typeQuestionService.partialUpdate(typeQuestion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeQuestion.getId().toString())
        );
    }

    /**
     * {@code GET  /type-questions} : get all the typeQuestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeQuestions in body.
     */
    @GetMapping("/type-questions")
    public List<TypeQuestion> getAllTypeQuestions( HttpServletRequest request) {
        log.debug("REST request to get all TypeQuestions");
        return typeQuestionService.findAll( request.getRemoteAddr() , ENTITY_NAME);
    }

    /**
     * {@code GET  /type-questions/:id} : get the "id" typeQuestion.
     *
     * @param id the id of the typeQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-questions/{id}")
    public ResponseEntity<TypeQuestion> getTypeQuestion(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to get TypeQuestion : {}", id);
        Optional<TypeQuestion> typeQuestion = typeQuestionService.findOne(id, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseUtil.wrapOrNotFound(typeQuestion);
    }

    /**
     * {@code DELETE  /type-questions/:id} : delete the "id" typeQuestion.
     *
     * @param id the id of the typeQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-questions/{id}")
    public ResponseEntity<Void> deleteTypeQuestion(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to delete TypeQuestion : {}", id);
        typeQuestionService.delete(id, request.getRemoteAddr() , ENTITY_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
