package com.sasd.eventor.event;

import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.EventUtils.*;

public class EventUpdateTest extends EventTest {

    @Test
    public void updateWithChanges() {
        var dto = validEventCreateDto(VALID_NAME);
        var event = eventController.create(dto);
        var updatedEvent = eventController.update(validEventUpdateDto(
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
    public void updateWithoutChanges() {
        var dto = validEventCreateDto(VALID_NAME);
        var event = eventController.create(dto);
        var updatedEvent = eventController.update(validEventUpdateDto(event.getId(), dto.getJwt()));

        assert updatedEvent.getId().equals(event.getId());
        assert updatedEvent.getName().equals(event.getName());
        assert updatedEvent.getDate().equals(event.getDate());
        assert updatedEvent.getDescription().equals(event.getDescription());
        assert updatedEvent.getLocation().equals(event.getLocation());
        assert updatedEvent.getPrice().equals(event.getPrice());
    }
}
