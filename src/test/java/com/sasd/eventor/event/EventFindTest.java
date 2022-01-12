package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class EventFindTest extends EventTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void findExistingEvent() {
        var event = new Event();
        event.setDate(VALID_DATE);
        event.setDescription(VALID_DESCRIPTION);
        event.setLocation(VALID_LOCATION);
        event.setPrice(VALID_PRICE);
        var expectedEvent = eventRepository.save(event);
        var foundEvent = eventController.findById(expectedEvent.getId());
        assert foundEvent.getDate().equals(expectedEvent.getDate());
        assert foundEvent.getDescription().equals(expectedEvent.getDescription());
        assert foundEvent.getLocation().equals(expectedEvent.getLocation());
        assert foundEvent.getPrice().equals(expectedEvent.getPrice());
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        Assertions.assertThrows(EventorException.class, () ->
           eventController.findById(new Random().nextLong())
        );
    }
}
