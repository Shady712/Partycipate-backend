package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.dtos.EventResponseDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final UserService userService;
    private final EventService eventService;
    private final ConversionService conversionService;

    @GetMapping("/findById")
    public EventResponseDto findById(@RequestParam Long id) {
        return conversionService.convert(
                eventService.findById(id)
                        .orElseThrow(() -> new EventorException("Event with provided id does not exist")),
                EventResponseDto.class
        );
    }

    @PostMapping("/create")
    public EventResponseDto create(@RequestBody @Valid EventCreateDto eventCreateDto) {
        if (userService.findByJwt(eventCreateDto.getJwt()).isEmpty()) {
            throw new EventorException("Creator does not exist");
        }
        return conversionService.convert(
                eventService.createEvent(conversionService.convert(eventCreateDto, Event.class)),
                EventResponseDto.class
        );
    }

    @GetMapping("/findAllByCreator")
    public List<EventResponseDto> findAllByCreator(@RequestParam String login) {
        return eventService.findAllByCreator(
                userService.findByLogin(login)
                   .orElseThrow(() -> new EventorException("User with provided login does not exist")))
                .stream()
                .map(event -> conversionService.convert(event, EventResponseDto.class))
                .toList();
    }

    @GetMapping("/findAllByNamePrefix")
    public List<EventResponseDto> findAllByNamePrefix(@RequestParam String prefix) {
        return eventService.findAllByNamePrefix(prefix)
                .stream()
                .map(event -> conversionService.convert(event, EventResponseDto.class))
                .toList();
    }
}
