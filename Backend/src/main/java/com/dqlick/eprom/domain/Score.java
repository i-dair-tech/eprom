package com.dqlick.eprom.domain;


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
@Schema(description = "Score entity.")
@Entity
@Table(name = "eprom_score")
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "score")
    private int score;

    @Column(name = "owner")
    private String owner;

    @Column(name = "created_date")
    private Instant createdDate;

    @OneToOne
    @JoinColumn(name = "invitation_id")
    private Invitation invitation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "survey_id", insertable = true, updatable = false, nullable = false)
    private Survey survey;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public String toString() {
        return "Score{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", owner='" + getOwner() + '\'' +
            ", createdDate=" + getCreatedDate() +
            ", invitation=" + getInvitation() +
            ", survey=" + getSurvey() +
            '}';
    }
}
