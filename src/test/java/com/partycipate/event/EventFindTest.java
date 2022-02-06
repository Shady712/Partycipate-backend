package com.partycipate.event;

import com.partycipate.utils.EventUtils;
import com.partycipate.exception.PartycipateException;
import com.partycipate.model.daos.EventRepository;
import com.partycipate.model.dtos.InviteResponseDto;
import com.partycipate.model.entities.Event;
import com.partycipate.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static com.partycipate.utils.UserUtils.*;

public class EventFindTest extends EventTest {

    @Test
    public void findExistingEvent(@Autowired EventRepository eventRepository) {
        var event = new Event();
        event.setName(EventUtils.VALID_NAME);
        event.setDate(EventUtils.VALID_DATE);
        event.setDescription(EventUtils.VALID_DESCRIPTION);
        event.setLocation(EventUtils.VALID_LOCATION);
        event.setPrice(EventUtils.VALID_PRICE);
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
        Assertions.assertThrows(PartycipateException.class, () ->
                eventController.findById(new Random().nextLong())
        );
    }

    @Test
    public void findAllEventsByOneCreator() {
        var eventCreateDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(getJwt());
        var event = eventController.create(eventCreateDto);
        var creator = event.getCreator();

        var eventCreateDtoSecond = EventUtils.validEventCreateDtoWithoutJwt(
                EventUtils.VALID_NAME + " Second",
                EventUtils.VALID_DATE,
                EventUtils.VALID_DESCRIPTION + " Second",
                EventUtils.VALID_LOCATION + " Second",
                EventUtils.VALID_PRICE + 2,
                EventUtils.VALID_LATITUDE,
                EventUtils.VALID_LONGITUDE
        );
        eventCreateDtoSecond.setJwt(eventCreateDto.getJwt());
        var eventSecond = eventController.create(eventCreateDtoSecond);
        var creatorSecond = eventSecond.getCreator();

        var eventCreateDtoThird = EventUtils.validEventCreateDtoWithoutJwt();
        eventCreateDtoThird.setJwt(getJwt(validUserRegisterDto()));
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
        var firstEvent = eventController.create(validEventCreateDto(EventUtils.VALID_NAME + "a"));
        var secondEvent = eventController.create(validEventCreateDto(EventUtils.VALID_NAME + "b"));
        var thirdEvent = eventController.create(validEventCreateDto("prefix" + EventUtils.VALID_NAME));

        var firstList = eventController.findAllByNamePrefix("");
        var secondList = eventController.findAllByNamePrefix(EventUtils.VALID_NAME);
        var thirdList = eventController.findAllByNamePrefix(EventUtils.VALID_NAME + "a");

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

    @Test
    public void findAllGuests() {
        var eventDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventDto.setJwt(getJwt());
        var eventId = eventController.create(eventDto).getId();
        assert Stream.of(
                validUserRegisterDto(),
                validUserRegisterDto(),
                validUserRegisterDto()
        ).map(dto -> inviteController.acceptInvite(
                        inviteController.createInvite(validInviteCreateDto(
                                registerUser(dto).getId(),
                                eventId,
                                eventDto.getJwt()
                        )).getId(),
                        userController.createJwt(dto.getLogin(), dto.getPassword())
                )
        ).map(InviteResponseDto::getReceiver).toList().equals(eventController.findAllGuests(eventId));
        var invitedGuest = registerUser();
        inviteController.createInvite(validInviteCreateDto(
                invitedGuest.getId(),
                eventId,
                eventDto.getJwt()
        ));
        assert !eventController.findAllGuests(eventId).contains(invitedGuest);
        assert !eventController.findAllGuests(eventId).contains(registerUser());
    }
}
