package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.EventResponseDto;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.EventUtils.*;

public class EventDeleteTest extends EventTest {

    @Test
    public void successfulDeleting() {
        var eventDtoWithJwt = new EventDtoWithJwt();
        eventController.delete(eventDtoWithJwt.getCreatedEvent().getId(), eventDtoWithJwt.getCreatorJwt());
        Assertions.assertThrows(EventorException.class,
                () -> eventController.findById(eventDtoWithJwt.getCreatedEvent().getId()));
    }

    @Test
    public void ensureBadRequestForDeniedPermission() {
        var eventDtoWithJwt = new EventDtoWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> eventController.delete(eventDtoWithJwt.getCreatedEvent().getId(), getJwt()));
    }

    @Test
    public void ensureBadRequestForDeletingUncreatedEvent() {
        var eventDtoWithJwt = new EventDtoWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> eventController.delete(eventDtoWithJwt.getCreatedEvent().getId() + 100,
                        eventDtoWithJwt.getCreatorJwt()));
    }

    @Getter
    private class EventDtoWithJwt {
        private final EventResponseDto createdEvent;
        private final String creatorJwt;

        private EventDtoWithJwt() {
            var eventCreateDto = validEventCreateDtoWithoutJwt();
            creatorJwt = getJwt();
            eventCreateDto.setJwt(creatorJwt);
            createdEvent = eventController.create(eventCreateDto);
        }
    }
}