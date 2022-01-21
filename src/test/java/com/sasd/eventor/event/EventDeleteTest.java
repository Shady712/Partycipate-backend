package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static com.sasd.eventor.utils.EventUtils.*;

public class EventDeleteTest extends EventTest {

    @Test
    public void ensureBadRequestForFindingDeletedEvent() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(validJwt());
        var createdEvent = eventController.create(eventCreateDto);
        eventController.deleteById(createdEvent.getId());
        Assertions.assertThrows(EventorException.class, () -> eventController.findById(createdEvent.getId()));
    }
}
