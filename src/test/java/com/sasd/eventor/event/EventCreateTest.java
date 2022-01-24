package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.EventUtils.*;

public class EventCreateTest extends EventTest {

    @Test
    public void createValidEvent() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(validJwt());
        var event = eventController.create(eventCreateDto);
        var creator = event.getCreator();
        var foundCreator = userController.enterByJwt(eventCreateDto.getJwt());
        assert creator.equals(foundCreator);
        assert event.getName().equals(VALID_NAME);
        assert event.getDate().equals(VALID_DATE);
        assert event.getLocation().equals(VALID_LOCATION);
        assert event.getDescription().equals(VALID_DESCRIPTION);
        assert event.getPrice().equals(VALID_PRICE);
        assert event.getGuests().isEmpty();
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
