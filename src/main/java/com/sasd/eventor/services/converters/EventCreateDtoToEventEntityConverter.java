package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@AllArgsConstructor
public class EventCreateDtoToEventEntityConverter implements Converter<EventCreateDto, Event> {
    private final UserService userService;

    @Override
    public Event convert(EventCreateDto source) {
        var record = new Event();
        record.setName(source.getName());
        record.setDate(source.getDate());
        record.setDescription(source.getDescription());
        record.setPrice(source.getPrice());
        record.setLocation(source.getLocation());
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        User creator = userService.findByJwt(source.getJwt()).get();
        record.setCreator(creator);
        record.setGuests(Collections.emptyList());
        return record;
    }
}
