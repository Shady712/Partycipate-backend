package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.EventUtils.*;

public class EventDeleteTest extends EventTest {

    @Test
    public void ensureBadRequestForDeniedPermission() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        var jwt = validJwt();
        eventCreateDto.setJwt(jwt);
        var createdEvent = eventController.create(eventCreateDto);
        Assertions.assertThrows(EventorException.class, () -> eventController.deleteById(createdEvent.getId(), validJwt()));
    }

    @Test
    public void ensureBadRequestForFindingDeletedEvent() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        var jwt = validJwt();
        eventCreateDto.setJwt(jwt);
        var createdEvent = eventController.create(eventCreateDto);
        eventController.deleteById(createdEvent.getId(), jwt);
        Assertions.assertThrows(EventorException.class, () -> eventController.findById(createdEvent.getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUncreatedEvent() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        var jwt = validJwt();
        eventCreateDto.setJwt(jwt);
        var createdEvent = eventController.create(eventCreateDto);
        Assertions.assertThrows(EventorException.class,
                () -> eventController.deleteById(createdEvent.getId() + 100, jwt));
    }
}