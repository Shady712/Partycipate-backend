package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.InviteRepository;
import com.sasd.eventor.model.entities.Invite;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;

    public Optional<Invite> findById(Long id) {
        return inviteRepository.findById(id);
    }

    public Invite create(Invite invite) {
        return inviteRepository.save(invite);
    }
}