package com.dqlick.eprom.service.dto;


import com.dqlick.eprom.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalAnswerDTO implements Serializable {


    private  Long id;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private Long invitationId;

    private Long surveyId;

    private List<Answer> answers;

    private int finalScore;


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

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public Long getSurveyId() { return surveyId; }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
