package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.UserService;
import com.sasd.eventor.services.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final UserService userService;
    private final EventService eventService;
    private final JwtService jwtService;
    private final ConversionService conversionService;

    @GetMapping("/findById")
    public Event findById(@RequestParam Long id) {
        return eventService.findById(id).orElseThrow(() -> new EventorException("Event with provided id does not exist"));
    }

    @PostMapping("/create")
    public Event create(@RequestBody @Valid EventCreateDto eventCreateDto) {
        if (userService.findById(jwtService.decodeJwtToId(eventCreateDto.getJwt())).isEmpty()) {
            throw new EventorException("Creator does not exists");
        }
        return eventService.createEvent(conversionService.convert(eventCreateDto, Event.class));
    }
}
