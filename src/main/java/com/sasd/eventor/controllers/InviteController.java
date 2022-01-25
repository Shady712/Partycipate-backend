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

    @DeleteMapping("/delete")
    public void deleteById(@RequestParam Long id, @RequestParam String jwt) {
        if (userService.findByJwt(jwt).isEmpty()
                || !(findById(id).getEvent().getCreator().getId().equals(userService.findByJwt(jwt).get().getId())
                    || findById(id).getReceiver().getId().equals(userService.findByJwt(jwt).get().getId()))) {
            throw new EventorException("You have no permission");
        } else {
            findById(id);
            inviteService.deleteById(id);
        }
    }
}
