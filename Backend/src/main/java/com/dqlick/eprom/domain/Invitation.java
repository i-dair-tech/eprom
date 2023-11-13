package com.dqlick.eprom.domain;

import com.dqlick.eprom.domain.enumeration.InvitationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "invitation entity.")
@Entity
@Table(name = "eprom_invitation")
public class Invitation  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;


    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254)
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "survey_id", insertable = true, updatable = false, nullable = false)
    private Survey survey;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvitationStatus status;


    @Column(name = "validity")
    private String validity;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "sender_email")
    private String senderEmail;

    @Column(name = "get_notified")
    private Boolean getNotified;

    @Column(name = "score_notif")
    private int scoreNotif;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinTable(
//        name = "rel_invitation__invitation_cron",
//        joinColumns = @JoinColumn(name = "invitation_id", updatable = true),
//        inverseJoinColumns = @JoinColumn(name = "invitation_cron_id" ,updatable = true)
//
//    )
//    @JsonIgnoreProperties(value = { "invitations" }, allowSetters = true)
//    private Set<InvitationCron> invitationCrons = new HashSet<>();



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
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

//    public Set<InvitationCron> getInvitationCrons() {
//        return invitationCrons;
//    }
//
//    public void setInvitationCrons(Set<InvitationCron> invitationCrons) {
//        this.invitationCrons = invitationCrons;
//    }


    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Boolean getGetNotified() {
        return getNotified;
    }

    public void setGetNotified(Boolean getNotified) {
        this.getNotified = getNotified;
    }

    public int getScoreNotif() {
        return scoreNotif;
    }

    public void setScoreNotif(int scoreNotif) {
        this.scoreNotif = scoreNotif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invitation)) {
            return false;
        }
        return id != null && id.equals(((Invitation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }



    @Override
    public String toString() {
        return "Invitation{" +
            "id=" + getId() +
            ", survey=" + getSurvey() +
            ", status=" + getStatus() +
            ", email='" + getEmail() + '\'' +
            ", validity=" + getValidity() +
            ", createdBy='" + getCreatedBy() + '\'' +
            ", createdDate=" + getCreatedDate() +
            //"invitationCrons=" + getInvitationCrons() +
            ", senderEmail=" +getSenderEmail() +
            ",getNotified"+ getGetNotified() +
            ",scoreNotif"+ getScoreNotif() +
            '}';
    }


}
