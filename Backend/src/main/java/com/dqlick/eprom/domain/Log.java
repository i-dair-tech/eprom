package com.dqlick.eprom.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The log entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Log entity.")
@Entity
@Table(name = "eprom_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private LogAction action;

    @Column(name = "entity")
    private String entity;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private LogOperation operation;

    @Column(name = "remote_ip")
    private String remoteIp;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Log id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogAction getAction() {
        return this.action;
    }

    public Log action(LogAction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(LogAction action) {
        this.action = action;
    }

    public String getEntity() {
        return this.entity;
    }

    public Log entity(String entity) {
        this.setEntity(entity);
        return this;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public LogOperation getOperation() {
        return this.operation;
    }

    public Log operation(LogOperation operation) {
        this.setOperation(operation);
        return this;
    }

    public void setOperation(LogOperation operation) {
        this.operation = operation;
    }

    public String getRemoteIp() {
        return this.remoteIp;
    }

    public Log remoteIp(String remoteIp) {
        this.setRemoteIp(remoteIp);
        return this;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getDescription() {
        return this.description;
    }

    public Log description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Log createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Log createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Log)) {
            return false;
        }
        return id != null && id.equals(((Log) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Log{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", entity='" + getEntity() + "'" +
            ", operation='" + getOperation() + "'" +
            ", remoteIp='" + getRemoteIp() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
