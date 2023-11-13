package com.dqlick.eprom.service;

import com.dqlick.eprom.domain.InvitationCron;
import com.dqlick.eprom.repository.InvitationCronRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InvitationCronService {

    private final InvitationCronRepository invitationCronRepository;

    public InvitationCronService(InvitationCronRepository invitationCronRepository) {
        this.invitationCronRepository = invitationCronRepository;
    }



    @Transactional
    public InvitationCron save (InvitationCron invitationCron){

        return invitationCronRepository.save(invitationCron);
    }




}
