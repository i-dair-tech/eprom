package com.dqlick.eprom.service.dto;

import com.dqlick.eprom.domain.Survey;
import com.dqlick.eprom.domain.enumeration.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

/**
 * A DTO for the {@link com.dqlick.eprom.domain.Invitation} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDTO implements Serializable {

    private  Long id;

    private Set<String> emails;

    private int survey_id;

    private  InvitationStatus status;

    private  String validity;

    private  String createdBy;

    private  Instant createdDate;

    private Set<Date> invitationCrons;

    private String senderEmail;

    private boolean getNotified;

    private int scoreNotif ;

    private String timeZone;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }


    public int getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(int survey_id) {
        this.survey_id = survey_id;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Date> getInvitationCrons() {
        return invitationCrons;
    }

    public void setInvitationCrons(Set<Date> invitationCrons) {
        this.invitationCrons = invitationCrons;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public boolean isGetNotified() {
        return getNotified;
    }

    public void setGetNotified(boolean getNotified) {
        this.getNotified = getNotified;
    }

    public int getScoreNotif() {
        return scoreNotif;
    }

    public void setScoreNotif(int scoreNotif) {
        this.scoreNotif = scoreNotif;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
