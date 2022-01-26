package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.InviteCreateDto;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.InviteService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/invite")
public class InviteController {
    private final InviteService inviteService;
    private final UserService userService;
    private final EventService eventService;
    private final ConversionService conversionService;

    @RequestMapping("/findById")
    public Invite findById(@RequestParam Long id) {
        return inviteService.findById(id)
                .orElseThrow(() -> new EventorException("Invite with provided id does not exist"));
    }

    @PostMapping("/create")
    public Invite create(@RequestBody @Valid InviteCreateDto inviteCreateDto) {
        if (userService.findById(inviteCreateDto.getReceiverId()).isEmpty() ||
                eventService.findById(inviteCreateDto.getEventId()).isEmpty()) {
            throw new EventorException("Invalid receiver id or event id");
        }
        return inviteService.create(conversionService.convert(inviteCreateDto, Invite.class));
    }

    @GetMapping("/findAllIncoming")
    public List<Invite> findAllIncoming(@RequestParam String jwt) {
        return inviteService.findAllIncoming(
                userService.findByJwt(jwt)
                        .orElseThrow(() -> new EventorException("You are not Authorized"))
        );
    }

    @GetMapping("/findAllByEventId")
    public List<Invite> findAllByEventId(@RequestParam Long eventId, String creatorJwt) {
        if (!userService.findByJwt(creatorJwt)
                .orElseThrow(() -> new EventorException("You are not authorized"))
                .equals(eventService.findById(eventId)
                        .orElseThrow(() -> new EventorException("Event with provided id does not exist"))
                        .getCreator()
                )
        ) {
            throw new EventorException("You do not have such permission");
        }
        return inviteService.findAllByEventId(eventService.findById(eventId).get());
    }
}
