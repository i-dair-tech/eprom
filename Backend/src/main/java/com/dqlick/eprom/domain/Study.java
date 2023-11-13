package com.dqlick.eprom.domain;

    import java.io.Serializable;
    import java.time.Instant;
    import java.util.HashSet;
    import java.util.Set;

    import javax.persistence.*;

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
@Table(name = "eprom_study")
public class Study implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

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
        name = "rel_study__survey",
        joinColumns = @JoinColumn(name = "study_id"),
        inverseJoinColumns = @JoinColumn(name = "survey_id")
    )
    private Set<Survey> surveys = new HashSet<>();



    // jhipster-needle-entity-add-field - JHipster will add fields here


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Boolean getIsArchived() {
        return this.isArchived;
    }

    public Study isArchived(Boolean isArchived) {
        this.setIsArchived(isArchived);
        return this;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Instant getArchivedDate() {
        return archivedDate;
    }


    public void setArchivedDate(Instant archivedDate) {
        this.archivedDate = archivedDate;
    }

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Study)) {
            return false;
        }
        return id != null && id.equals(((Study) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return "Study{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", isArchived=" + isArchived +
            ", archivedDate=" + archivedDate +
            ", surveys=" + surveys +
            '}';
    }
}
