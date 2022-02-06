package com.partycipate.services;

import com.partycipate.model.daos.InviteRepository;
import com.partycipate.model.entities.Event;
import com.partycipate.model.entities.Invite;
import com.partycipate.model.entities.User;
import com.partycipate.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.partycipate.model.enums.RequestStatus.ACCEPTED;
import static com.partycipate.model.enums.RequestStatus.REJECTED;

@Service
@AllArgsConstructor
public class InviteService {
    private final EventService eventService;
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

    public List<Invite> findAllByEvent(Event event) {
        logger.debug("Received request for finding all invites on event '{}', event id is '{}",
                event.getName(), event.getId()
        );
        return inviteRepository.findAllByEvent(event);
    }

    public Invite acceptInvite(Invite invite) {
        logger.info("Accepted invite '{}'", invite.getId());
        invite.setStatus(ACCEPTED);
        eventService.addGuest(invite.getEvent(), invite.getReceiver());
        return inviteRepository.save(invite);
    }

    public Invite rejectInvite(Invite invite) {
        logger.info("Rejected invite '{}'", invite.getId());
        invite.setStatus(REJECTED);
        return inviteRepository.save(invite);
    }

    public void deleteInvite(Invite invite) {
        logger.info("Deleting invite with id '{}' on event '{}', event id is '{}', sender is '{}', receiver is '{}",
                invite.getId(), invite.getEvent().getName(), invite.getEvent().getId(),
                invite.getEvent().getCreator().getLogin(), invite.getReceiver().getLogin()
        );
        inviteRepository.delete(invite);
    }
}