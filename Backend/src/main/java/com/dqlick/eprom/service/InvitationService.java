package com.dqlick.eprom.service;

import com.dqlick.eprom.domain.Invitation;
import com.dqlick.eprom.service.dto.InvitationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InvitationService {
    /**
     * Save a invitation.
     *
     * @param invitation the entity to save.
     */
    void create(InvitationDTO invitation,MultipartFile file,  String ipAdress , String entity,String username, String password) throws FileNotFoundException;




    /**
     * Get all the invitation.
     *
     * @return the list of entities.
     */
    List<Invitation> findAllByEmail(String email);



    /**
     * Get all the invitation.
     *
     * @return the list of entities.
     */
    List<Invitation> findDistinctBySurveyID(String email);


    /**
     * Get the "id" invitation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Invitation> findOne(Long id, String ipAdress , String entity);


    /**
     * Get the "id" invitation.
     *
     * @param email and surveyId the id of the entity.
     * @return the entity.
     */
    Optional<Invitation> findOneByEmailAndSurvey(String email , Long surveyId);

    /**
     * Delete the "id" invitation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, String ipAdress , String entity);


    List<Map<String, Object>> getSurveysAndCount();

    String generateCronExpression(Date date);
}
