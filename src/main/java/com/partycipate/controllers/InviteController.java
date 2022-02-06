package com.partycipate.controllers;

import com.partycipate.exception.PartycipateException;
import com.partycipate.services.EventService;
import com.partycipate.services.InviteService;
import com.partycipate.services.UserService;
import com.partycipate.model.dtos.InviteCreateDto;
import com.partycipate.model.dtos.InviteResponseDto;
import com.partycipate.model.entities.Invite;
import com.partycipate.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.partycipate.model.enums.RequestStatus.REJECTED;
import static com.partycipate.model.enums.RequestStatus.WAITING;

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
                .orElseThrow(() -> new PartycipateException("Invalid event id"));
        if (!event.getCreator().equals(userService.findByJwt(inviteCreateDto.getCreatorJwt())
                .orElseThrow(() -> new PartycipateException("You are not authorized"))
        )) {
            throw new PartycipateException("You do not have such permission");
        }
        if (inviteService.findAllIncoming(userService.findById(inviteCreateDto.getReceiverId())
                        .orElseThrow(() -> new PartycipateException("Invalid receiver id")))
                .stream()
                .anyMatch(invite -> invite.getEvent()
                        .equals(event))
        ) {
            throw new PartycipateException("You have already sent an invite to this user");
        }
        return conversionService.convert(
                inviteService.create(Objects.requireNonNull(conversionService.convert(inviteCreateDto, Invite.class))),
                InviteResponseDto.class
        );
    }

    @RequestMapping("/findById")
    public InviteResponseDto findById(@RequestParam Long id, @RequestParam String userJwt) {
        var invite = inviteService.findById(id)
                .orElseThrow(() -> new PartycipateException("Invite with provided id does not exist"));
        var user = userService.findByJwt(userJwt)
                .orElseThrow(() -> new PartycipateException("You are not authorized"));
        if (!user.equals(invite.getReceiver()) && !user.equals(invite.getEvent().getCreator()))
            throw new PartycipateException("You do not have such permission");
        return conversionService.convert(
                invite,
                InviteResponseDto.class
        );
    }

    @GetMapping("/findAllIncoming")
    public List<InviteResponseDto> findAllIncoming(@RequestParam String jwt) {
        return inviteService.findAllIncoming(
                        userService.findByJwt(jwt)
                                .orElseThrow(() -> new PartycipateException("You are not authorized"))
                )
                .stream()
                .map(invite -> conversionService.convert(invite, InviteResponseDto.class))
                .toList();
    }

    @GetMapping("/findAllByEventId")
    public List<InviteResponseDto> findAllByEventId(@RequestParam Long eventId, @RequestParam String creatorJwt) {
        var foundEvent = eventService.findById(eventId);
        if (!userService.findByJwt(creatorJwt)
                .orElseThrow(() -> new PartycipateException("You are not authorized"))
                .equals(foundEvent
                        .orElseThrow(() -> new PartycipateException("Event with provided id does not exist"))
                        .getCreator()
                )
        ) {
            throw new PartycipateException("You do not have such permission");
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
        if (!foundInvite.orElseThrow(() -> new PartycipateException("Invite with provided id does not exist"))
                .getEvent().getCreator().equals(userService.findByJwt(eventCreatorJwt)
                        .orElseThrow(() -> new PartycipateException("You are not authorized")))
        ) {
            throw new PartycipateException("You do not have such permission");
        }
        inviteService.deleteInvite(foundInvite.get());
    }

    private Invite getValidatedInvite(Long id, String receiverJwt, Set<RequestStatus> statuses) {
        var foundInvite = inviteService.findById(id);
        if (!foundInvite.orElseThrow(() -> new PartycipateException("Invite with provided id does not exist"))
                .getReceiver().equals(userService.findByJwt(receiverJwt)
                        .orElseThrow(() -> new PartycipateException("You are not Authorized")))
                || !statuses.contains(foundInvite.get().getStatus())
        ) {
            throw new PartycipateException("You do not have such permission");
        }
        return foundInvite.get();
    }
}
