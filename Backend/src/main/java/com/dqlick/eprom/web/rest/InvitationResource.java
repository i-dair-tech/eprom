package com.dqlick.eprom.web.rest;


import com.dqlick.eprom.Shared.SharedObjectService;
import com.dqlick.eprom.domain.Invitation;
import com.dqlick.eprom.repository.InvitationRepository;
import com.dqlick.eprom.service.InvitationService;
import com.dqlick.eprom.service.MailService;

import com.dqlick.eprom.service.dto.InvitationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InvitationResource {

    private final Logger log = LoggerFactory.getLogger(InvitationResource.class);

    private static final String ENTITY_NAME = "invitation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvitationService invitationService;

    private final InvitationRepository invitationRepository;

    private final MailService mailService;

    private SharedObjectService sharedObjectService ;
    
    public InvitationResource(InvitationService invitationService, InvitationRepository invitationRepository, MailService mailService,SharedObjectService sharedObjectService) {
        this.invitationService = invitationService;
        this.invitationRepository = invitationRepository;
        this.mailService = mailService;
        this.sharedObjectService = sharedObjectService;
    }


    @PostMapping("/invitations" )
    public void createInvitation(@RequestParam("data")String invitation, @RequestParam(value = "file", required =
        false) MultipartFile file, HttpServletRequest request,String username,String password) throws URISyntaxException, FileNotFoundException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        InvitationDTO object = new ObjectMapper().readValue(invitation, InvitationDTO.class);
            invitationService.create(object, file, request.getRemoteAddr(), ENTITY_NAME,username,password);

    }



    /**
     * {@code GET  /invitations/:email} : get all the invitations by email.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invitations in body.
     */
    @GetMapping("/invitations/{email}")
    public List<Invitation> getAllInvitationsByEmail(@PathVariable String email) {
        log.debug("REST request to get all Invitations by email");

        return invitationService.findAllByEmail(email);
    }



    /**
     * {@code GET  /invitations} : get all the invitations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invitations in body.
     */
    @GetMapping("/invitations")
    public List<Invitation> getAllInvitations() {
        log.debug("REST request to get all Invitations");
        return invitationRepository.findAll();
    }


    /**
     * {@code GET  /invitations} : get all the distinct invitations .
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invitations in body.
     */
    @GetMapping("/invitations/distinct/{email}")
    public List<Invitation> getAllDistinctInvitationsBySurveyId(@PathVariable String email) {
        log.debug("REST request to get all distinct Invitations");

        return invitationService.findDistinctBySurveyID(email);
    }




/*
    @PostMapping("/invitations-date")
    public String getCron(@Valid @RequestBody Date date) throws URISyntaxException {

       return invitationService.generateCronExpression(date);

    }*/


     //count

    /**
     * {@code GET  /invitations} : get count of all the invitations with SENT status.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invitations in body.
     */
    @GetMapping("/invitations-sent-total")
    public int getCountSentInvitation() {
        log.debug("REST request to get all Invitations");
        return invitationRepository.findCountByStatusSent();
    }


    /**
     * {@code GET  /invitations} : get count of all the invitations with NOT ANSWERED status.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invitations in body.
     */
    @GetMapping("/invitations-not-answered-total")
    public int getCountIgnoredInvitation() {
        log.debug("REST request to get all Invitations");
        return invitationRepository.findCountByStatusNotAnswered();
    }




}
