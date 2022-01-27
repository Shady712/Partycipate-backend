package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.EventResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.EventUtils.*;

public class EventDeleteTest extends EventTest {

    @Test
    public void ensureBadRequestForDeniedPermission() {
        var createdEventData = new createdEventData();
        Assertions.assertThrows(EventorException.class,
                () -> eventController.deleteById(createdEventData.getCreatedEvent().getId(), validJwt()));
    }

    @Test
    public void ensureBadRequestForFindingDeletedEvent() {
        var createdEventData = new createdEventData();
        eventController.deleteById(createdEventData.getCreatedEvent().getId(), createdEventData.getCreatorJwt());
        Assertions.assertThrows(EventorException.class,
                () -> eventController.findById(createdEventData.getCreatedEvent().getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUncreatedEvent() {
        var createdEventData = new createdEventData();
        Assertions.assertThrows(EventorException.class,
                () -> eventController.deleteById(createdEventData.getCreatedEvent().getId() + 100,
                        createdEventData.getCreatorJwt()));
    }

    private class createdEventData {
        private final EventResponseDto createdEvent;
        private final String creatorJwt;

        private createdEventData() {
            var eventCreateDto = validEventCreateDtoWithoutJwt();
            creatorJwt = validJwt();
            eventCreateDto.setJwt(creatorJwt);
            createdEvent = eventController.create(eventCreateDto);
        }

        public EventResponseDto getCreatedEvent() {
            return createdEvent;
        }

        public String getCreatorJwt() {
            return creatorJwt;
        }
    }
}