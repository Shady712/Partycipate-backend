package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.EventRepository;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deleteById(Long id){
        eventRepository.deleteById(id);
    }

    public List<Event> findAllByCreator(User user) {
        return eventRepository.findAllByCreator(user);
    }

    public List<Event> findAllByNamePrefix(String prefix) {
        return eventRepository.findByNameStartingWith(prefix);
    }

    public void deleteById(Long id){
        eventRepository.deleteById(id);
    }
}
