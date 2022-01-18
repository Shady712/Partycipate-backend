package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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

        var eventCreateDtoSecond = validEventCreateDtoWithoutJwt(VALID_NAME + " Second",
                VALID_DATE,
                VALID_DESCRIPTION + " Second",
                VALID_LOCATION + " Second",
                VALID_PRICE + 2);
        eventCreateDtoSecond.setJwt(eventCreateDto.getJwt());
        var eventSecond = eventController.create(eventCreateDtoSecond);
        var creatorSecond = eventSecond.getCreator();

        var eventCreateDtoThird = validEventCreateDtoWithoutJwt();
        eventCreateDtoThird.setJwt(validJwt(VALID_USER_LOGIN + " Second",
                                            VALID_NAME + " Second",
                                            VALID_USER_PASSWORD + " Second"));
        var eventThird = eventController.create(eventCreateDtoThird);
        var creatorThird = eventThird.getCreator();
        var firstList = eventController.findAllByCreator(creator.getLogin());
        var secondList = eventController.findAllByCreator(creatorThird.getLogin());

        assert firstList.contains(event);
        assert firstList.contains(eventSecond);
        assert firstList.equals(eventController.findAllByCreator(creatorSecond.getLogin()));
        assert !firstList.contains(eventThird);
        assert !secondList.contains(event);
        assert !secondList.contains(eventSecond);
        assert secondList.contains(eventThird);

    }
}
