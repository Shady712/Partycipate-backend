package com.sasd.eventor.services.converters;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import com.sasd.eventor.services.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventCreateDtoToEventEntityConverter implements Converter<EventCreateDto, Event> {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public Event convert(EventCreateDto source) {
        var record = new Event();
        record.setName(source.getName());
        record.setDate(source.getDate());
        record.setDescription(source.getDescription());
        record.setPrice(source.getPrice());
        record.setLocation(source.getLocation());
        User creator = userService.findById(jwtService.decodeJwtToId(source.getJwt()))
                        .orElseThrow(() -> new EventorException("Creator does not exist"));
        record.setCreator(creator);
        return record;
    }
}
