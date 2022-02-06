package com.partycipate.event;

import com.partycipate.exception.PartycipateException;
import com.partycipate.utils.EventUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventUpdateTest extends EventTest {

    @Test
    public void validUpdateWithChanges() {
        var dto = validEventCreateDto(EventUtils.VALID_NAME);
        var event = eventController.create(dto);
        var updatedEvent = eventController.update(EventUtils.validEventUpdateDto(
                event.getId(),
                event.getName() + "change",
                event.getDate(),
                event.getDescription() + "change",
                event.getLocation() + "change",
                event.getPrice() + 100,
                dto.getJwt()
        ));

        assert updatedEvent.getId().equals(event.getId());
        assert updatedEvent.getName().equals(event.getName() + "change");
        assert updatedEvent.getDate().equals(event.getDate());
        assert updatedEvent.getDescription().equals(event.getDescription() + "change");
        assert updatedEvent.getLocation().equals(event.getLocation() + "change");
        assert updatedEvent.getPrice().equals(event.getPrice() + 100);
    }

    @Test
    public void validUpdateWithoutChanges() {
        var dto = validEventCreateDto(EventUtils.VALID_NAME);
        var event = eventController.create(dto);
        var updatedEvent = eventController.update(EventUtils.validEventUpdateDto(event.getId(), dto.getJwt()));

        assert updatedEvent.getId().equals(event.getId());
        assert updatedEvent.getName().equals(event.getName());
        assert updatedEvent.getDate().equals(event.getDate());
        assert updatedEvent.getDescription().equals(event.getDescription());
        assert updatedEvent.getLocation().equals(event.getLocation());
        assert updatedEvent.getPrice().equals(event.getPrice());
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        Assertions.assertThrows(PartycipateException.class, () ->
                eventController.update(EventUtils.validEventUpdateDto(
                        eventController.create(validEventCreateDto(EventUtils.VALID_NAME)).getId(),
                        "invalid jwt"
                ))
        );
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        Assertions.assertThrows(PartycipateException.class, () ->
                eventController.update(EventUtils.validEventUpdateDto(
                        0L,
                        validEventCreateDto(EventUtils.VALID_NAME).getJwt()
                ))
        );
    }
}
