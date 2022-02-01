package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.dtos.EventResponseDto;
import com.sasd.eventor.model.dtos.EventUpdateDto;
import com.sasd.eventor.model.dtos.UserResponseDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.InviteService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final UserService userService;
    private final EventService eventService;
    private final ConversionService conversionService;
    private final InviteService inviteService;

    @PostMapping("/create")
    public EventResponseDto create(@RequestBody @Valid EventCreateDto eventCreateDto) {
        if (userService.findByJwt(eventCreateDto.getJwt()).isEmpty()) {
            throw new EventorException("You are not authorized");
        }
        return conversionService.convert(
                eventService.createEvent(Objects.requireNonNull(conversionService.convert(eventCreateDto, Event.class))),
                EventResponseDto.class
        );
    }

    @GetMapping("/findById")
    public EventResponseDto findById(@RequestParam Long id) {
        return conversionService.convert(
                eventService.findById(id)
                        .orElseThrow(() -> new EventorException("Event with provided id does not exist")),
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

    @GetMapping("/findAllGuests")
    public List<UserResponseDto> findAllGuests(@RequestParam Long id) {
        return eventService.findAllGuests(eventService.findById(id)
                        .orElseThrow(() -> new EventorException("Event with provided id does not exist"))
                )
                .stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .toList();
    }

    @PutMapping("/update")
    public EventResponseDto update(@RequestBody @Valid EventUpdateDto eventUpdateDto) {
        if (!userService.findByJwt(eventUpdateDto.getJwt())
                .orElseThrow(() -> new EventorException("You are not authorized")).
                equals(eventService.findById(eventUpdateDto.getId())
                        .orElseThrow(() -> new EventorException("Event with provided id does not exist")).getCreator())
        ) {
            throw new EventorException("You do not have such permission");
        }
        return conversionService.convert(
                eventService.update(conversionService.convert(eventUpdateDto, Event.class)),
                EventResponseDto.class
        );
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id, @RequestParam String jwt) {
        var event = eventService.findById(id)
                .orElseThrow(() -> new EventorException("Event with provided id does not exist"));
        if (!event.getCreator().equals(userService.findByJwt(jwt)
                        .orElseThrow(() -> new EventorException("You are not authorized")))
        ) {
            throw new EventorException("You do not have such permission");
        }
        inviteService.findAllByEvent(event).forEach(inviteService::deleteInvite);
        eventService.deleteEvent(event);
    }
}
