package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.InviteRepository;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(InviteService.class);

    public Optional<Invite> findById(Long id) {
        logger.debug("Received request for finding invite with id '{}'", id);
        return inviteRepository.findById(id);
    }

    public Invite create(Invite invite) {
        logger.info("Creating invite on event '{}', event id is '{}, sender is '{}', receiver is '{}'",
                invite.getEvent().getName(), invite.getEvent().getId(),
                invite.getEvent().getCreator().getLogin(), invite.getReceiver().getLogin()
        );
        mailService.sendEmail(
                invite.getReceiver().getEmail(),
                "New invite!",
                "You have been invited to event '" + invite.getEvent().getName() + "'! It's time to PARTYcipate!"
        );
        return inviteRepository.save(invite);
    }

    public List<Invite> findAllIncoming(User receiver) {
        logger.debug("Received request for finding all incoming invites for receiver '{}'", receiver.getLogin());
        return inviteRepository.findAllByReceiver(receiver);
    }

    public List<Invite> findAllByEventId(Event event) {
        logger.debug("Received request for finding all invites on event '{}', event id is '{}",
                event.getName(), event.getId()
        );
        return inviteRepository.findAllByEvent(event);
    }

    public void delete(Invite invite) {
        logger.info("Deleting invite with id '{}' on event '{}', event id is '{}', sender is '{}', receiver is '{}",
                invite.getId(), invite.getEvent().getName(), invite.getEvent().getId(),
                invite.getEvent().getCreator().getLogin(), invite.getReceiver().getLogin()
        );
        inviteRepository.delete(invite);
    }
}