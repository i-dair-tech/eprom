package com.dqlick.eprom.domain;

import com.dqlick.eprom.domain.enumeration.Language;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Question.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eprom_question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @ManyToMany(fetch = FetchType.EAGER,cascade =CascadeType.MERGE)
    @JoinTable(
        name = "rel_question__answer_choice",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_choice_id")
    )
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    private Set<AnswerChoice> answerChoices = new HashSet<>();


    @ManyToOne(fetch= FetchType.EAGER)
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    @JoinColumn(name = "type_question_id")
    private TypeQuestion typeQuestion;

    @ManyToMany(mappedBy = "questions")
    @JsonIgnore
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    private Set<Survey> surveys = new HashSet<>();


    //Getter & setter & others

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Question text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Question createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Question createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Question lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Question lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }



    public Language getLanguage() {
        return this.language;
    }

    public Question language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<AnswerChoice> getAnswerChoices() {
        return this.answerChoices;
    }

    public void setAnswerChoices(Set<AnswerChoice> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public Question answerChoices(Set<AnswerChoice> answerChoices) {
        this.setAnswerChoices(answerChoices);
        return this;
    }

    public Question addAnswerChoice(AnswerChoice answerChoice) {
        this.answerChoices.add(answerChoice);
        answerChoice.getQuestions().add(this);
        return this;
    }

    public Question removeAnswerChoice(AnswerChoice answerChoice) {
        this.answerChoices.remove(answerChoice);
        answerChoice.getQuestions().remove(this);
        return this;
    }

    public TypeQuestion getTypeQuestion() {
        return this.typeQuestion;
    }

    public void setTypeQuestion(TypeQuestion typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public Question typeQuestion(TypeQuestion typeQuestion) {
        this.setTypeQuestion(typeQuestion);
        return this;
    }

    public Set<Survey> getSurveys() {
        return this.surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        if (this.surveys != null) {
            this.surveys.forEach(i -> i.removeQuestion(this));
        }
        if (surveys != null) {
            surveys.forEach(i -> i.addQuestion(this));
        }
        this.surveys = surveys;
    }

    public Question surveys(Set<Survey> surveys) {
        this.setSurveys(surveys);
        return this;
    }

    public Question addSurvey(Survey survey) {
        this.surveys.add(survey);
        survey.getQuestions().add(this);
        return this;
    }

    public Question removeSurvey(Survey survey) {
        this.surveys.remove(survey);
        survey.getQuestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
/**
 public Question(String text, String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate, Boolean isArchived, Instant archivedDate, Language language, Set<AnswerChoice> answerChoices, TypeQuestion typeQuestion, Set<Survey> surveys) {

 this.text = text;
 this.createdBy = createdBy;
 this.createdDate = createdDate;
 this.lastModifiedBy = lastModifiedBy;
 this.lastModifiedDate = lastModifiedDate;
 this.isArchived = isArchived;
 this.archivedDate = archivedDate;
 this.language = language;
 this.answerChoices = answerChoices;
 this.typeQuestion = typeQuestion;
 this.surveys = surveys;
 }
 **/
}
