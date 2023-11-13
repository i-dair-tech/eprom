package com.dqlick.eprom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.Buffer;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eprom_invitation_document")
public class InvitationDocument  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "path")
    private String path;

    @Column(name = "data")
    private Byte[] data;

    @Column(name = "extension")
    private String extension;

    @Column(name = "size")
    private Number size;

    @Column(name = "externalPath")
    private String externalPath;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;






}
