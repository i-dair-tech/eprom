package com.dqlick.eprom.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Survey.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eprom_survey")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "description")
    private String description;

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

    @ManyToMany
    @JoinTable(
        name = "rel_survey__question",
        joinColumns = @JoinColumn(name = "survey_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    //@JsonIgnoreProperties(value = { "answerChoices", "typeQuestion", "surveys" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here


/*
    @ManyToOne
    @JoinColumn(name="study_id", nullable=false)
    private Study study;
*/


    public Long getId() {
        return this.id;
    }

    public Survey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Survey title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return this.topic;
    }

    public Survey topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return this.description;
    }

    public Survey description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getCreatedBy() {
        return this.createdBy;
    }

    public Survey createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Survey createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Survey lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Survey lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getIsArchived() {
        return this.isArchived;
    }

    public Survey isArchived(Boolean isArchived) {
        this.setIsArchived(isArchived);
        return this;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Instant getArchivedDate() {
        return this.archivedDate;
    }

    public Survey archivedDate(Instant archivedDate) {
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
        this.questions = questions;
    }

    public Survey questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Survey addQuestion(Question question) {
        this.questions.add(question);
        question.getSurveys().add(this);
        return this;
    }

    public Survey removeQuestion(Question question) {
        this.questions.remove(question);
        question.getSurveys().remove(this);
        return this;
    }

/*    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }*/


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Survey)) {
            return false;
        }
        return id != null && id.equals(((Survey) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Survey{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", topic='" + getTopic() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", isArchived='" + getIsArchived() + "'" +
            ", archivedDate='" + getArchivedDate() + "'" +
            "}";
    }
}
