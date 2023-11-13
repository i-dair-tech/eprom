package com.dqlick.eprom.repository;


import com.dqlick.eprom.domain.Invitation;
import com.dqlick.eprom.domain.InvitationCron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvitationCronRepository extends JpaRepository<InvitationCron, Long>, CrudRepository<InvitationCron, Long> {

    @Query(value ="select * from eprom_invitation_cron where  invitation_id = :invitationId ", nativeQuery = true)
    List<InvitationCron> findAllByInvitationId(@Param("invitationId") Long invitationId);

}
