package com.dqlick.eprom.service.dto;

import com.dqlick.eprom.domain.AnswerChoice;
import com.dqlick.eprom.domain.Invitation;
import com.dqlick.eprom.domain.Question;
import com.dqlick.eprom.domain.Survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO implements Serializable {


    private Long id;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private Instant createdDate;

    private Boolean isArchived;

    private Instant archivedDate;

    private Question question;

    private Survey survey;

    private Invitation invitation;

    private List<AnswerChoiceDTO> answerChoices;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public Instant getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Instant archivedDate) {
        this.archivedDate = archivedDate;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    public List<AnswerChoiceDTO> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(List<AnswerChoiceDTO> answerChoices) {
        this.answerChoices = answerChoices;
    }
}
