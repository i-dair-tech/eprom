package com.dqlick.eprom.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.dqlick.eprom.domain.enumeration.Language;
import com.dqlick.eprom.service.dto.AnswerChoiceDTO;
import com.dqlick.eprom.service.dto.SurveyDTO;
import com.dqlick.eprom.service.dto.TypeQuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO implements Serializable {
    private Long id;
    @NotNull
    private String text;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
    @NotNull
    private Language language;
    private Set<AnswerChoiceDTO> answerChoices = new HashSet<>();
    private TypeQuestionDTO typeQuestion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<AnswerChoiceDTO> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(Set<AnswerChoiceDTO> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public TypeQuestionDTO getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(TypeQuestionDTO typeQuestion) {
        this.typeQuestion = typeQuestion;
    }
}
