package com.dqlick.eprom.domain;

import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;


/**
 * The log entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Log Patient entity.")
@Entity
@Table(name = "eprom_log_patient")
public class LogPatient implements Serializable {

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogAction getAction() {
        return action;
    }

    public void setAction(LogAction action) {
        this.action = action;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public LogOperation getOperation() {
        return operation;
    }

    public void setOperation(LogOperation operation) {
        this.operation = operation;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
