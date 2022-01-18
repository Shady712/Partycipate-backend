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
        event.setName(VALID_NAME);
        event.setDate(VALID_DATE);
        event.setDescription(VALID_DESCRIPTION);
        event.setLocation(VALID_LOCATION);
        event.setPrice(VALID_PRICE);
        var expectedEvent = eventRepository.save(event);
        var foundEvent = eventController.findById(expectedEvent.getId());
        assert foundEvent.getName().equals(expectedEvent.getName());
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

    @Test
    public void findAllEventsByOneCreator() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(validJwt());
        var event = eventController.create(eventCreateDto);
        var creator = event.getCreator();

        var eventCreateDtoSnd = validEventCreateDtoWithoutJwtSnd();
        eventCreateDtoSnd.setJwt(eventCreateDto.getJwt());
        var eventSnd = eventController.create(eventCreateDtoSnd);
        var creatorSnd = eventSnd.getCreator();

        var eventCreateDtoTrd = validEventCreateDtoWithoutJwt();
        eventCreateDtoTrd.setJwt(validJwtSnd());
        var eventTrd = eventController.create(eventCreateDtoTrd);
        var creatorTrd = eventTrd.getCreator();

        assert eventController.findAllByCreator(creator.getLogin()).contains(event);
        assert eventController.findAllByCreator(creator.getLogin()).contains(eventSnd);
        assert !eventController.findAllByCreator(creator.getLogin()).contains(eventTrd);
        assert eventController.findAllByCreator(creatorSnd.getLogin()).contains(event);
        assert eventController.findAllByCreator(creatorSnd.getLogin()).contains(eventSnd);
        assert !eventController.findAllByCreator(creatorSnd.getLogin()).contains(eventTrd);
        assert !eventController.findAllByCreator(creatorTrd.getLogin()).contains(event);
        assert !eventController.findAllByCreator(creatorTrd.getLogin()).contains(eventSnd);
        assert eventController.findAllByCreator(creatorTrd.getLogin()).contains(eventTrd);

    }
}
