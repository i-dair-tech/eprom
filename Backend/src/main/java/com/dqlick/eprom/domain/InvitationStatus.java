package com.dqlick.eprom.domain;

import com.dqlick.eprom.domain.enumeration.ScheduleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "invitation cron date entity.")
    @Entity
    @Table(name = "eprom_invitation_status")
    public class InvitationStatus implements Serializable {


        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        @Column(name = "id")
        private Long id;

        @Column(name = "status")
        private String status;




}
