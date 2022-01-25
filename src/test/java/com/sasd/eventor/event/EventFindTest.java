package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        event.setGuests(List.of(validUser(), validUser(), validUser()));
        event.setCreator(validUser());
        var expectedEvent = eventRepository.save(event);
        var foundEvent = eventController.findById(expectedEvent.getId());
        assert foundEvent.getName().equals(expectedEvent.getName());
        assert foundEvent.getDate().equals(expectedEvent.getDate());
        assert foundEvent.getDescription().equals(expectedEvent.getDescription());
        assert foundEvent.getLocation().equals(expectedEvent.getLocation());
        assert foundEvent.getPrice().equals(expectedEvent.getPrice());
        assert foundEvent.getGuests().equals(expectedEvent.getGuests()
                .stream()
                .map(User::getLogin)
                .toList());
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

    @Test
    public void findAllEventsByNamePrefix() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setName(VALID_NAME + "a");
        eventCreateDto.setJwt(validJwt());
        var firstEvent = eventController.create(eventCreateDto);
        eventCreateDto.setName(VALID_NAME + "b");
        eventCreateDto.setJwt(validJwt());
        var secondEvent = eventController.create(eventCreateDto);
        eventCreateDto.setName("Nonexistential flex");
        eventCreateDto.setJwt(validJwt());
        var thirdEvent = eventController.create(eventCreateDto);

        var firstList = eventController.findAllByNamePrefix("");
        var secondList = eventController.findAllByNamePrefix(VALID_NAME);
        var thirdList = eventController.findAllByNamePrefix(VALID_NAME + "a");

        assert firstList.contains(firstEvent);
        assert firstList.contains(secondEvent);
        assert firstList.contains(thirdEvent);
        assert secondList.contains(firstEvent);
        assert secondList.contains(secondEvent);
        assert !secondList.contains(thirdEvent);
        assert thirdList.contains(firstEvent);
        assert !thirdList.contains(secondEvent);
        assert !thirdList.contains(thirdEvent);
    }
}
