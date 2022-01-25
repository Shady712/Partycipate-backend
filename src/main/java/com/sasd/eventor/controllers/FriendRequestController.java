package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.FriendRequestCreateDto;
import com.sasd.eventor.model.dtos.FriendRequestResponseDto;
import com.sasd.eventor.model.entities.FriendRequest;
import com.sasd.eventor.services.FriendRequestService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/friendRequest")
public class FriendRequestController {
    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final ConversionService conversionService;

    @PostMapping("/create")
    public FriendRequestResponseDto createRequest(@RequestBody @Valid FriendRequestCreateDto friendRequestCreateDto) {
        if (userService.findByJwt(friendRequestCreateDto.getSenderJwt()).isEmpty()
                || userService.findByLogin(friendRequestCreateDto.getReceiverLogin()).isEmpty()) {
            throw new EventorException("Invalid sender or receiver");
        }
        if (findAllOutgoing(friendRequestCreateDto.getSenderJwt())
                .stream()
                .anyMatch(friendRequestResponseDto ->
                        friendRequestResponseDto.getReceiverLogin().equals(friendRequestCreateDto.getReceiverLogin()))
        ) {
            throw new EventorException("You have already sent a friend request to user "
                    + friendRequestCreateDto.getReceiverLogin());
        }
        return conversionService.convert(
                friendRequestService.create(conversionService.convert(friendRequestCreateDto, FriendRequest.class)),
                FriendRequestResponseDto.class
        );
    }

    @GetMapping("/findById")
    public FriendRequestResponseDto findById(@RequestParam Long id) {
        return conversionService.convert(
                friendRequestService.findById(id)
                        .orElseThrow(() -> new EventorException("Friend request with provided id does not exist")),
                FriendRequestResponseDto.class
        );
    }

    @GetMapping("/findAllIncoming")
    public List<FriendRequestResponseDto> findAllIncoming(@RequestParam String receiverJwt) {
        return friendRequestService
                .findAllIncoming(userService.findByJwt(receiverJwt)
                        .orElseThrow(() -> new EventorException("You are not authorized")))
                .stream()
                .map(friendRequest -> conversionService.convert(friendRequest, FriendRequestResponseDto.class))
                .toList();
    }

    @GetMapping("/findAllOutgoing")
    public List<FriendRequestResponseDto> findAllOutgoing(@RequestParam String senderJwt) {
        return friendRequestService
                .findAllOutgoing(userService.findByJwt(senderJwt)
                        .orElseThrow(() -> new EventorException("You are not authorized")))
                .stream()
                .map(friendRequest -> conversionService.convert(friendRequest, FriendRequestResponseDto.class))
                .toList();
    }
}
