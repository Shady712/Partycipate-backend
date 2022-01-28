package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.EventUpdateDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.services.EventService;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventUpdateDtoToEventEntityConverter implements Converter<EventUpdateDto, Event> {
    private final EventService eventService;

    @Override
    public Event convert(EventUpdateDto source) {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        var record = eventService.findById(source.getId()).get();
        record.setName(source.getName());
        record.setDate(source.getDate());
        record.setDescription(source.getDescription());
        record.setLocation(source.getLocation());
        record.setPrice(source.getPrice());
        record.setLatitude(source.getLatitude());
        record.setLongitude(source.getLongitude());
        return record;
    }
}
