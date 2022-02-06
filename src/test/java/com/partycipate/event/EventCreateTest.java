package com.partycipate.event;

import com.partycipate.utils.EventUtils;
import com.partycipate.exception.PartycipateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventCreateTest extends EventTest {

    @Test
    public void createValidEvent() {
        var eventCreateDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(getJwt());
        var event = eventController.create(eventCreateDto);
        var creator = event.getCreator();
        assert creator.equals(userController.enterByJwt(eventCreateDto.getJwt()));
        assert event.getName().equals(EventUtils.VALID_NAME);
        assert event.getDate().equals(EventUtils.VALID_DATE);
        assert event.getLocation().equals(EventUtils.VALID_LOCATION);
        assert event.getDescription().equals(EventUtils.VALID_DESCRIPTION);
        assert event.getPrice().equals(EventUtils.VALID_PRICE);
        assert event.getGuests().isEmpty();
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        var eventCreateDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt("Invalid jwt");
        Assertions.assertThrows(PartycipateException.class, () ->
            eventController.create(eventCreateDto)
        );
    }
}
