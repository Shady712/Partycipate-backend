package com.partycipate.controllers;

import com.partycipate.services.FriendRequestService;
import com.partycipate.services.UserService;
import com.partycipate.exception.PartycipateException;
import com.partycipate.model.dtos.FriendRequestCreateDto;
import com.partycipate.model.dtos.FriendRequestResponseDto;
import com.partycipate.model.entities.FriendRequest;
import com.partycipate.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.partycipate.model.enums.RequestStatus.REJECTED;
import static com.partycipate.model.enums.RequestStatus.WAITING;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/friendRequest")
public class FriendRequestController {
    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final ConversionService conversionService;

    private static final Set<RequestStatus> ACCEPTABLE_STATUSES = Set.of(WAITING, REJECTED);
    private static final Set<RequestStatus> REJECTABLE_STATUSES = Set.of(WAITING);

    @Transactional
    @PostMapping("/create")
    public FriendRequestResponseDto createRequest(@RequestBody @Valid FriendRequestCreateDto friendRequestCreateDto) {
        final FriendRequestResponseDto[] response = new FriendRequestResponseDto[1];
        findAllIncoming(friendRequestCreateDto.getSenderJwt()).stream()
                .filter(friendRequestResponseDto -> friendRequestResponseDto.getSender().getLogin()
                        .equals(friendRequestCreateDto.getReceiverLogin())
                )
                .findFirst()
                .ifPresentOrElse(
                        reverseRequest -> response[0] = acceptRequest(
                                reverseRequest.getId(),
                                friendRequestCreateDto.getSenderJwt()
                        ),
                        () -> response[0] = createValidatedRequest(friendRequestCreateDto)
                );
        return response[0];
    }

    @GetMapping("/findById")
    public FriendRequestResponseDto findById(@RequestParam Long id) {
        return conversionService.convert(
                friendRequestService.findById(id)
                        .orElseThrow(() -> new PartycipateException("Friend request with provided id does not exist")),
                FriendRequestResponseDto.class
        );
    }

    @GetMapping("/findAllIncoming")
    public List<FriendRequestResponseDto> findAllIncoming(@RequestParam String receiverJwt) {
        return friendRequestService
                .findAllIncoming(userService.findByJwt(receiverJwt)
                        .orElseThrow(() -> new PartycipateException("You are not authorized")))
                .stream()
                .map(friendRequest -> conversionService.convert(friendRequest, FriendRequestResponseDto.class))
                .toList();
    }

    @GetMapping("/findAllOutgoing")
    public List<FriendRequestResponseDto> findAllOutgoing(@RequestParam String senderJwt) {
        return friendRequestService
                .findAllOutgoing(userService.findByJwt(senderJwt)
                        .orElseThrow(() -> new PartycipateException("You are not authorized")))
                .stream()
                .map(friendRequest -> conversionService.convert(friendRequest, FriendRequestResponseDto.class))
                .toList();
    }

    @Transactional
    @PutMapping("/accept")
    public FriendRequestResponseDto acceptRequest(@RequestParam Long id, @RequestParam String receiverJwt) {
        return conversionService.convert(
                friendRequestService.acceptRequest(getValidatedRequest(id, receiverJwt, ACCEPTABLE_STATUSES)),
                FriendRequestResponseDto.class
        );
    }

    @Transactional
    @PutMapping("/reject")
    public FriendRequestResponseDto rejectRequest(@RequestParam Long id, @RequestParam String receiverJwt) {
        return conversionService.convert(
                friendRequestService.rejectRequest(getValidatedRequest(id, receiverJwt, REJECTABLE_STATUSES)),
                FriendRequestResponseDto.class
        );
    }

    @DeleteMapping("/delete")
    public void deleteRequest(@RequestParam Long id, @RequestParam String senderJwt) {
        var foundRequest = friendRequestService.findById(id);
        if (!foundRequest.orElseThrow(() -> new PartycipateException("Friend request with provided id does not exist"))
                .getSender().equals(userService.findByJwt(senderJwt)
                        .orElseThrow(() -> new PartycipateException("You are not authorized")))
        ) {
            throw new PartycipateException("You do not have such permission");
        }
        friendRequestService.deleteRequest(foundRequest.get());
    }

    private FriendRequest getValidatedRequest(Long id, String receiverJwt, Set<RequestStatus> statuses) {
        var foundRequest = friendRequestService.findById(id);
        if (!foundRequest.orElseThrow(() -> new PartycipateException("Friend request with provided id does not exist"))
                .getReceiver().equals(userService.findByJwt(receiverJwt)
                        .orElseThrow(() -> new PartycipateException("You are not authorized")))
                || !statuses.contains(foundRequest.get().getStatus())
        ) {
            throw new PartycipateException("You do not have such permission");
        }
        return foundRequest.get();
    }

    private FriendRequestResponseDto createValidatedRequest(FriendRequestCreateDto friendRequestCreateDto) {
        if (userService.findByLogin(friendRequestCreateDto.getReceiverLogin()).isEmpty()) {
            throw new PartycipateException("Invalid receiver");
        }

        if (findAllOutgoing(friendRequestCreateDto.getSenderJwt())
                .stream()
                .anyMatch(friendRequestResponseDto ->
                        friendRequestResponseDto.getReceiver().getLogin()
                                .equals(friendRequestCreateDto.getReceiverLogin()))
        ) {
            throw new PartycipateException("You have already sent a friend request to user "
                    + friendRequestCreateDto.getReceiverLogin());
        }

        return conversionService.convert(
                friendRequestService.create(
                        Objects.requireNonNull(conversionService.convert(
                                friendRequestCreateDto, FriendRequest.class
                        ))),
                FriendRequestResponseDto.class
        );
    }
}
