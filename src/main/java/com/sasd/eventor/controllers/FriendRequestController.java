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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

import static com.sasd.eventor.model.enums.RequestStatus.WAITING;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/friendRequest")
public class FriendRequestController {
    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final ConversionService conversionService;

    @Transactional
    @PostMapping("/create")
    public FriendRequestResponseDto createRequest(@RequestBody @Valid FriendRequestCreateDto friendRequestCreateDto) {
        var reverseRequest = findAllIncoming(friendRequestCreateDto.getSenderJwt())
                .stream()
                .filter(friendRequestResponseDto ->
                        friendRequestResponseDto.getSenderLogin().equals(friendRequestCreateDto.getReceiverLogin()))
                .findFirst();
        if (reverseRequest.isPresent()) {
            return acceptRequest(reverseRequest.get().getId(), friendRequestCreateDto.getSenderJwt());
        }

        if (userService.findByJwt(friendRequestCreateDto.getSenderJwt()).isEmpty()
                || userService.findByLogin(friendRequestCreateDto.getReceiverLogin()).isEmpty()
        ) {
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

    @Transactional
    @PutMapping("/accept")
    public FriendRequestResponseDto acceptRequest(@RequestParam Long id, @RequestParam String receiverJwt) {
        return conversionService.convert(
                friendRequestService.acceptRequest(getValidatedRequest(id, receiverJwt)),
                FriendRequestResponseDto.class
        );
    }

    @Transactional
    @PutMapping("/reject")
    public FriendRequestResponseDto rejectRequest(@RequestParam Long id, @RequestParam String receiverJwt) {
        return conversionService.convert(
                friendRequestService.rejectRequest(getValidatedRequest(id, receiverJwt)),
                FriendRequestResponseDto.class
        );
    }

    // TODO: test this
    @DeleteMapping("/delete")
    public void deleteRequest(@RequestParam Long id, @RequestParam String senderJwt) {
        var foundRequest = friendRequestService.findById(id);
        var foundSender = userService.findByJwt(senderJwt);
        if (foundRequest.isEmpty() || foundSender.isEmpty()
                || foundRequest.get().getSender().getLogin().equals(foundSender.get().getLogin())
        ) {
            throw new EventorException("You do not have such permission");
        }
        friendRequestService.deleteRequest(foundRequest.get());
    }

    private FriendRequest getValidatedRequest(Long id, String receiverJwt) {
        var foundRequest = friendRequestService.findById(id);
        var foundReceiver = userService.findByJwt(receiverJwt);
        if (foundRequest.isEmpty() || foundReceiver.isEmpty()
                || !foundRequest.get().getReceiver().getLogin().equals(foundReceiver.get().getLogin())
                || !foundRequest.get().getStatus().equals(WAITING)
        ) {
            throw new EventorException("You do not have such permission");
        }
        return foundRequest.get();
    }
}
