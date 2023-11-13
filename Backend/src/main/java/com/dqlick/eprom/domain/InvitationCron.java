package com.dqlick.eprom.domain;


import com.dqlick.eprom.domain.enumeration.ScheduleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "invitation cron date entity.")
@Entity
@Table(name = "eprom_invitation_cron")
public class InvitationCron implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cron_job_date")
    private Date cronDate;

    @Column(name = "status")
    private ScheduleStatus status;



    @ManyToOne(fetch= FetchType.EAGER)
    @JsonIgnoreProperties(value = { "invitations" }, allowSetters = true)
    @JoinColumn(name = "invitation_id")
    private Invitation invitation;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCronDate() {
        return cronDate;
    }

    public void setCronDate(Date cronDate) {
        this.cronDate = cronDate;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    @Override
    public String toString() {
        return "InvitationCron{" +
            "id=" + getId() +
            ", cronDate=" + getCronDate() +
            ", status=" + getStatus() +
            ", invitation"+getInvitation() +
            '}';
    }



}
