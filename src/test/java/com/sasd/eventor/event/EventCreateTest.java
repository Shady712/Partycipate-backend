package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EventCreateTest extends EventTest {
    @Autowired
    protected UserService userService;

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void createValidEvent() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(validJwt());
        var event = eventController.create(eventCreateDto);
        var creator = event.getCreator();
        var foundCreator = userService.findByJwt(eventCreateDto.getJwt());
        assert foundCreator.isPresent();
        assert foundCreator.get().equals(creator);
        assert event.getName().equals(VALID_NAME);
        assert event.getDate().equals(VALID_DATE);
        assert event.getLocation().equals(VALID_LOCATION);
        assert event.getDescription().equals(VALID_DESCRIPTION);
        assert event.getPrice().equals(VALID_PRICE);
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt("Invalid jwt");
        Assertions.assertThrows(EventorException.class, () ->
            eventController.create(eventCreateDto)
        );
    }
}
