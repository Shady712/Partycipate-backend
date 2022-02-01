package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.InviteCreateDto;
import com.sasd.eventor.model.dtos.InviteResponseDto;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.model.enums.RequestStatus;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.InviteService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.sasd.eventor.model.enums.RequestStatus.REJECTED;
import static com.sasd.eventor.model.enums.RequestStatus.WAITING;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/invite")
public class InviteController {
    private final InviteService inviteService;
    private final UserService userService;
    private final EventService eventService;
    private final ConversionService conversionService;

    private static final Set<RequestStatus> ACCEPTABLE_STATUSES = Set.of(WAITING, REJECTED);
    private static final Set<RequestStatus> REJECTABLE_STATUSES = Set.of(WAITING);

    @PostMapping("/create")
    public InviteResponseDto createInvite(@RequestBody @Valid InviteCreateDto inviteCreateDto) {
        var event = eventService.findById(inviteCreateDto.getEventId())
                .orElseThrow(() -> new EventorException("Invalid event id"));
        if (inviteService.findAllIncoming(userService.findById(inviteCreateDto.getReceiverId())
                        .orElseThrow(() -> new EventorException("Invalid receiver id")))
                .stream()
                .anyMatch(invite -> invite.getEvent()
                        .equals(event))
        ) {
            throw new EventorException("You have already sent an invite to this user");
        }
        return inviteService.create(Objects.requireNonNull(conversionService.convert(inviteCreateDto, Invite.class)));
        return conversionService.convert(
                inviteService.create(Objects.requireNonNull(conversionService.convert(inviteCreateDto, Invite.class))),
                InviteResponseDto.class
        );
    }

    @GetMapping("/findById")
    public Invite findById(@RequestParam Long id) {
        return inviteService.findById(id)
                .orElseThrow(() -> new EventorException("Invite with provided id does not exist"));
    @RequestMapping("/findById")
    public InviteResponseDto findById(@RequestParam Long id, @RequestParam String userJwt) {
        var invite = inviteService.findById(id)
                .orElseThrow(() -> new EventorException("Invite with provided id does not exist"));
        var user = userService.findByJwt(userJwt)
                .orElseThrow(() -> new EventorException("You are not authorized"));
        if (!user.equals(invite.getReceiver()) && !user.equals(invite.getEvent().getCreator()))
            throw new EventorException("You do not have such permission");
        return conversionService.convert(
                invite,
                InviteResponseDto.class
        );
    }

    @GetMapping("/findAllIncoming")
    public List<InviteResponseDto> findAllIncoming(@RequestParam String jwt) {
        return inviteService.findAllIncoming(
                        userService.findByJwt(jwt)
                                .orElseThrow(() -> new EventorException("You are not authorized"))
                )
                .stream()
                .map(invite -> conversionService.convert(invite, InviteResponseDto.class))
                .toList();
    }

    @GetMapping("/findAllByEventId")
    public List<InviteResponseDto> findAllByEventId(@RequestParam Long eventId, @RequestParam String creatorJwt) {
        var foundEvent = eventService.findById(eventId);
        if (!userService.findByJwt(creatorJwt)
                .orElseThrow(() -> new EventorException("You are not authorized"))
                .equals(foundEvent
                        .orElseThrow(() -> new EventorException("Event with provided id does not exist"))
                        .getCreator()
                )
        ) {
            throw new EventorException("You do not have such permission");
        }
        return inviteService.findAllByEvent(foundEvent.get())
                .stream()
                .map(invite -> conversionService.convert(invite, InviteResponseDto.class))
                .toList();
    }

    @PutMapping("/accept")
    public InviteResponseDto acceptInvite(@RequestParam Long id, @RequestParam String receiverJwt) {
        return conversionService.convert(
                inviteService.acceptInvite(getValidatedInvite(id, receiverJwt, ACCEPTABLE_STATUSES)),
                InviteResponseDto.class
        );
    }

    @PutMapping("/reject")
    public InviteResponseDto rejectInvite(@RequestParam Long id, @RequestParam String receiverJwt) {
        return conversionService.convert(
                inviteService.rejectInvite(getValidatedInvite(id, receiverJwt, REJECTABLE_STATUSES)),
                InviteResponseDto.class
        );
    }

    @DeleteMapping("/delete")
    public void deleteInvite(@RequestParam Long id, @RequestParam String eventCreatorJwt) {
        var foundInvite = inviteService.findById(id);
        if (!foundInvite.orElseThrow(() -> new EventorException("Invite with provided id does not exist"))
                .getEvent().getCreator().equals(userService.findByJwt(eventCreatorJwt)
                        .orElseThrow(() -> new EventorException("You are not authorized")))
        ) {
            throw new EventorException("You do not have such permission");
        }
        inviteService.deleteInvite(foundInvite.get());
    }

    private Invite getValidatedInvite(Long id, String receiverJwt, Set<RequestStatus> statuses) {
        var foundInvite = inviteService.findById(id);
        if (!foundInvite.orElseThrow(() -> new EventorException("Invite with provided id does not exist"))
                .getReceiver().equals(userService.findByJwt(receiverJwt)
                        .orElseThrow(() -> new EventorException("You are not Authorized")))
                || !statuses.contains(foundInvite.get().getStatus())
        ) {
            throw new EventorException("You do not have such permission");
        }
        return foundInvite.get();
    }
}
