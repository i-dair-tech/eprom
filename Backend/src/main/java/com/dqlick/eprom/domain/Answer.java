package com.dqlick.eprom.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Answer.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eprom_answer")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254)
    private String email;

    @Column(name = "created_date")
    private Instant createdDate;


    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "archived_date")
    private Instant archivedDate;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", insertable = true, updatable = false, nullable = false)
    private Question question;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "survey_id", insertable = true, updatable = false, nullable = false)
    private Survey survey;

    @OneToOne
    @JoinColumn(name = "invitation_id")
    private Invitation invitation;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(
        name = "rel_answer__answer_choice",
        joinColumns = @JoinColumn(name = "answer_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_choice_id")
    )
    @JsonIgnoreProperties(value = { "answers" }, allowSetters = true)
    private List<AnswerChoice> answerChoices = new ArrayList<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean archived) {
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

    public List<AnswerChoice> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(List<AnswerChoice> answerChoices) {
        this.answerChoices = answerChoices;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return id != null && id.equals(((Answer) o).id);
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", email='" + getEmail() + '\'' +
            ", createdDate=" + getCreatedDate() +
            ", isArchived=" + getIsArchived() +
            ", archivedDate=" + getArchivedDate() +
            ", question=" + getQuestion() +
            ", survey=" + getSurvey() +
            ", invitation=" + getInvitation() +
            ", answerChoices=" + getAnswerChoices() +
            '}';
    }
}
