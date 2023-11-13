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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A TypeQuestion.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eprom_type_question")
public class TypeQuestion implements Serializable {

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

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "archived_date")
    private Instant archivedDate;


    @JsonIgnore
    @OneToMany(mappedBy = "typeQuestion")
    //@JsonIgnoreProperties(value = { "answerChoices", "typeQuestion", "surveys" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

       public Long getId() {
        return this.id;
    }

    public TypeQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public TypeQuestion text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public TypeQuestion createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public TypeQuestion createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public TypeQuestion lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public TypeQuestion lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getIsArchived() {
        return this.isArchived;
    }

    public TypeQuestion isArchived(Boolean isArchived) {
        this.setIsArchived(isArchived);
        return this;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Instant getArchivedDate() {
        return this.archivedDate;
    }

    public TypeQuestion archivedDate(Instant archivedDate) {
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
            this.questions.forEach(i -> i.setTypeQuestion(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setTypeQuestion(this));
        }
        this.questions = questions;
    }

    public TypeQuestion questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public TypeQuestion addQuestion(Question question) {
        this.questions.add(question);
        question.setTypeQuestion(this);
        return this;
    }

    public TypeQuestion removeQuestion(Question question) {
        this.questions.remove(question);
        question.setTypeQuestion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeQuestion)) {
            return false;
        }
        return id != null && id.equals(((TypeQuestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeQuestion{" +
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
