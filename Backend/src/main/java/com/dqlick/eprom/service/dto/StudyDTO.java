package com.dqlick.eprom.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyDTO implements Serializable {

    private  Long id;
    @NotNull
    private  String title;
    private  String createdBy;
    private  Instant createdDate;
    private  String lastModifiedBy;
    private  Instant lastModifiedDate;
    private  Boolean isArchived;
    private  Instant archivedDate;
    private  Set<SurveyDTO> surveys;

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

    public Set<SurveyDTO> getSurveys() {
        return surveys;
    }

    public void setSurveys(Set<SurveyDTO> surveys) {
        this.surveys = surveys;
    }
}
