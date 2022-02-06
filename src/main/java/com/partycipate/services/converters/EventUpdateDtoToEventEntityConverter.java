package com.partycipate.services.converters;

import com.partycipate.model.dtos.EventUpdateDto;
import com.partycipate.model.entities.Event;
import com.partycipate.services.EventService;

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
        record.setLat(source.getCoordinates().getLat());
        record.setLng(source.getCoordinates().getLng());
        record.setTelegramUrl(source.getTelegramUrl());
        return record;
    }
}
