package com.sasd.eventor.friendrequest;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.FriendRequestResponseDto;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class FriendRequestFindTest extends FriendRequestTest {

    @Test
    public void findExistingRequestById() {
        var expectedRequest = friendRequestController.createRequest(validFriendRequestCreateDto());
        var foundRequest = friendRequestController.findById(expectedRequest.getId());
        assert expectedRequest.getSenderLogin().equals(foundRequest.getSenderLogin());
        assert expectedRequest.getReceiverLogin().equals(foundRequest.getReceiverLogin());
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        var request = friendRequestController.createRequest(validFriendRequestCreateDto());
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.findById(request.getId() + 100)
        );
    }

    @Test
    public void findAllOutgoingRequests() {
        var senderDto = validUserRegisterDto();
        abstractFindAllTest(
                () -> senderDto,
                UserUtils::validUserRegisterDto,
                () -> friendRequestController.findAllOutgoing(getJwt(senderDto))
        );
    }

    @Test
    public void findAllIncomingRequests() {
        var receiverDto = validUserRegisterDto();
        abstractFindAllTest(
                UserUtils::validUserRegisterDto,
                () -> receiverDto,
                () -> friendRequestController.findAllIncoming(getJwt(receiverDto))
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        friendRequestController.createRequest(validFriendRequestCreateDto());
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.findAllIncoming("Invalid jwt")
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.findAllOutgoing("Invalid jwt")
        );
    }

    private void abstractFindAllTest(
            UserRegisterDtoFactory senderDtoFactory,
            UserRegisterDtoFactory receiverDtoFactory,
            JwtHandler jwtHandler
    ) {
        assert Stream.of(
                validFriendRequestCreateDto(senderDtoFactory.dto(), receiverDtoFactory.dto()),
                validFriendRequestCreateDto(senderDtoFactory.dto(), receiverDtoFactory.dto()),
                validFriendRequestCreateDto(senderDtoFactory.dto(), receiverDtoFactory.dto())
        )
                .map(friendRequestCreateDto -> friendRequestController.createRequest(friendRequestCreateDto))
                .toList()
                .equals(jwtHandler.handleJwt());
        assert !jwtHandler.handleJwt().contains(friendRequestController.createRequest(validFriendRequestCreateDto()));
    }

    private interface UserRegisterDtoFactory {
        UserRegisterDto dto();
    }

    private interface JwtHandler {
        List<FriendRequestResponseDto> handleJwt();
    }
}
