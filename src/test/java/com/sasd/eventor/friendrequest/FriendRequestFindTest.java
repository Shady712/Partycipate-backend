package com.sasd.eventor.friendrequest;

import org.junit.jupiter.api.Test;

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
    public void findAllOutgoingRequests() {
        var senderDto = validUserRegisterDto();
        var senderJwt = getJwt(senderDto);
        assert Stream.of(
                validFriendRequestCreateDto(senderDto, validUserRegisterDto()),
                validFriendRequestCreateDto(senderDto, validUserRegisterDto()),
                validFriendRequestCreateDto(senderDto, validUserRegisterDto())
        )
                .map(friendRequestCreateDto -> friendRequestController.createRequest(friendRequestCreateDto))
                .toList()
                .equals(friendRequestController.findAllOutgoing(senderJwt));
        assert !friendRequestController.findAllOutgoing(senderJwt)
                .contains(friendRequestController.createRequest(validFriendRequestCreateDto()));
    }
}
