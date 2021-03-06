package com.partycipate.services;

import com.partycipate.model.daos.EventRepository;
import com.partycipate.model.entities.Event;
import com.partycipate.model.entities.User;
import com.partycipate.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(EventService.class);

    public Optional<Event> findById(Long id) {
        logger.debug("Received request for finding event with id '{}'", id);
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        logger.info("Creating event '{}', creator is '{}'",
                event.getName(), event.getCreator().getLogin()
        );
        mailService.sendEmail(
                event.getCreator().getEmail(),
                "Event created!",
                "Congratulations, your event '" + event.getName() + "' has been successfully arranged. Time to " +
                        "invite some friends!"
        );
        return eventRepository.save(event);
    }

    public List<Event> findAllByCreator(User creator) {
        logger.debug("Received request for finding all events by creator '{}'", creator.getLogin());
        return eventRepository.findAllByCreator(creator);
    }

    public Event update(Event event) {
        logger.info("Updating event '{}', event id is '{}'", event.getName(), event.getId());
        return eventRepository.save(event);
    }

    public List<Event> findAllByNamePrefix(String prefix) {
        logger.debug("Received request for finding all events by name prefix '{}'", prefix);
        return eventRepository.findByNameStartingWith(prefix);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public List<User> findAllGuests(Event event) {
        logger.debug("Received request for finding all guests of eventId '{}'", event.getId());
        return eventRepository.findById(event.getId()).get().getGuests();
    }

    public void addGuest(Event event, User receiver) {
        logger.info("Adding new guest '{}', to event with '{}' id", receiver.getLogin(), event.getId());
        event.getGuests().add(receiver);
        eventRepository.save(event);
    }

    public void deleteEvent(Event event) {
        logger.info("Deleting event '{}', id is '{}', creator is '{}'",
                event.getName(), event.getId(), event.getCreator().getLogin()
        );
        eventRepository.delete(event);
    }
}
