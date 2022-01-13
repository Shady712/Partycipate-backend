package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.EventRepository;
import com.sasd.eventor.model.entities.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
}
