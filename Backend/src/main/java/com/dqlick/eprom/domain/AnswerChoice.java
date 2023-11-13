package com.dqlick.eprom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A AnswerChoice.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eprom_answer_choice")
public class AnswerChoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "archived_date")
    private Instant archivedDate;

    @ManyToMany(mappedBy = "answerChoices")
    @JsonIgnoreProperties(value = { "answerChoices", "typeQuestion", "surveys" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnswerChoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public AnswerChoice text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AnswerChoice createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AnswerChoice createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public AnswerChoice lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public AnswerChoice lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getIsArchived() {
        return this.isArchived;
    }

    public AnswerChoice isArchived(Boolean isArchived) {
        this.setIsArchived(isArchived);
        return this;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Instant getArchivedDate() {
        return this.archivedDate;
    }

    public AnswerChoice archivedDate(Instant archivedDate) {
        this.setArchivedDate(archivedDate);
        return this;
    }

    public void setArchivedDate(Instant archivedDate) {
        this.archivedDate = archivedDate;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.removeAnswerChoice(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addAnswerChoice(this));
        }
        this.questions = questions;
    }

    public AnswerChoice questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public AnswerChoice addQuestion(Question question) {
        this.questions.add(question);
        question.getAnswerChoices().add(this);
        return this;
    }

    public AnswerChoice removeQuestion(Question question) {
        this.questions.remove(question);
        question.getAnswerChoices().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerChoice)) {
            return false;
        }
        return id != null && id.equals(((AnswerChoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerChoice{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", isArchived='" + getIsArchived() + "'" +
            ", archivedDate='" + getArchivedDate() + "'" +
            "}";
    }
}
