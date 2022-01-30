package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.EventRepository;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final MailService mailService;

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        mailService.sendEmail(
                event.getCreator().getEmail(),
                "Event created!",
                "Congratulations, your event '" + event.getName() + "' has been successfully arranged. Time to " +
                        "invite some friends!"
        );
        return eventRepository.save(event);
    }

    public List<Event> findAllByCreator(User user) {
        return eventRepository.findAllByCreator(user);
    }

    public Event update(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findAllByNamePrefix(String prefix) {
        return eventRepository.findByNameStartingWith(prefix);
    }

    public void delete(Event event) {
        eventRepository.delete(event);
    }
}
