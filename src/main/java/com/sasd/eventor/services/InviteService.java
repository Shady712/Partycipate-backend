package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.InviteRepository;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Invite> findAllIncoming(User receiver) {
        return inviteRepository.findAllByReceiver(receiver);
    }

    public List<Invite> findAllByEventId(Event event) {
        return inviteRepository.findAllByEvent(event);
    }

    public void deleteById(Long id) {
        inviteRepository.deleteById(id);
    }
}