package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Random;

import static com.sasd.eventor.utils.EventUtils.*;
import static com.sasd.eventor.utils.UserUtils.*;

public class EventFindTest extends EventTest {

    @Test
    public void findExistingEvent() {
        var event = new Event();
        event.setName(VALID_NAME);
        event.setDate(VALID_DATE);
        event.setDescription(VALID_DESCRIPTION);
        event.setLocation(VALID_LOCATION);
        event.setPrice(VALID_PRICE);
        event.setGuests(Collections.emptyList());
        // TODO
        event.setCreator(userRepository.findById(userController.register(validUserRegisterDto()).getId())
                .orElseThrow(() -> new EventorException("Registration does not work"))
        );
        var expectedEvent = eventRepository.save(event);
        var foundEvent = eventController.findById(expectedEvent.getId());
        assert foundEvent.getName().equals(expectedEvent.getName());
        assert foundEvent.getDate().equals(expectedEvent.getDate());
        assert foundEvent.getDescription().equals(expectedEvent.getDescription());
        assert foundEvent.getLocation().equals(expectedEvent.getLocation());
        assert foundEvent.getPrice().equals(expectedEvent.getPrice());
        assert foundEvent.getGuests().equals(Collections.emptyList());
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

        var eventCreateDtoSecond = validEventCreateDtoWithoutJwt(
                VALID_NAME + " Second",
                VALID_DATE,
                VALID_DESCRIPTION + " Second",
                VALID_LOCATION + " Second",
                VALID_PRICE + 2
        );
        eventCreateDtoSecond.setJwt(eventCreateDto.getJwt());
        var eventSecond = eventController.create(eventCreateDtoSecond);
        var creatorSecond = eventSecond.getCreator();

        var eventCreateDtoThird = validEventCreateDtoWithoutJwt();
        eventCreateDtoThird.setJwt(validJwt(validUserRegisterDto()));
        var eventThird = eventController.create(eventCreateDtoThird);
        var creatorThird = eventThird.getCreator();
        var firstList = eventController.findAllByCreator(creator.getLogin());
        var secondList = eventController.findAllByCreator(creatorThird.getLogin());

        assert firstList.contains(event);
        assert firstList.contains(eventSecond);
        assert !firstList.contains(eventThird);
        assert firstList.equals(eventController.findAllByCreator(creatorSecond.getLogin()));
        assert !secondList.contains(event);
        assert !secondList.contains(eventSecond);
        assert secondList.contains(eventThird);
    }
}
