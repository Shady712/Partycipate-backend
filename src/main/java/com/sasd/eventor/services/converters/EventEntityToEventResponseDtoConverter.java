package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.EventResponseDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventEntityToEventResponseDtoConverter implements Converter<Event, EventResponseDto> {
    private final UserEntityToUserResponseDtoConverter userEntityToUserResponseDtoConverter;

    @Override
    public EventResponseDto convert(Event source){
        var record = new EventResponseDto();
        record.setId(source.getId());
        record.setName(source.getName());
        record.setDate(source.getDate());
        record.setLocation(source.getLocation());
        record.setDescription(source.getDescription());
        record.setPrice(source.getPrice());
        record.setCreator(
                userEntityToUserResponseDtoConverter.convert(source.getCreator())
        );
        record.setGuests(
                source.getGuests()
                        .stream()
                        .map(User::getLogin)
                        .toList()
        );
        record.setLatitude(source.getLatitude());
        record.setLongitude(source.getLongitude());
        return record;
    }
}
