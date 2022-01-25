package com.sasd.eventor.friendrequest;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.model.enums.RequestStatus.ACCEPTED;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class FriendRequestCreateTest extends FriendRequestTest {

    @Test
    public void createValidFriendRequest() {
        var senderDto = validUserRegisterDto();
        var receiverDto = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(senderDto, receiverDto)
        );
        assert request.getSenderLogin().equals(senderDto.getLogin());
        assert request.getReceiverLogin().equals(receiverDto.getLogin());
    }

    @Test
    public void ensureNoNewRequestForReverseRequest() {
        var firstDto = validUserRegisterDto();
        var secondDto = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(firstDto, secondDto)
        );

        friendRequestController.createRequest(
                validFriendRequestCreateDto(secondDto, firstDto)
        );

        assert friendRequestController.findAllOutgoing(getJwt(secondDto)).isEmpty();
        assert friendRequestController.findById(request.getId()).getStatus().equals(ACCEPTED);
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        var dto = validFriendRequestCreateDto();
        dto.setSenderJwt("Invalid jwt");
        Assertions.assertThrows(EventorException.class, () -> friendRequestController.createRequest(dto));
    }

    @Test
    public void ensureBadRequestForInvalidLogin() {
        var dto = validFriendRequestCreateDto();
        dto.setReceiverLogin("Invalid login");
        Assertions.assertThrows(EventorException.class, () -> friendRequestController.createRequest(dto));
    }

    @Test
    public void ensureBadRequestForRepeatedRequest() {
        var senderDto = validUserRegisterDto();
        var receiverDto = validUserRegisterDto();
        friendRequestController.createRequest(validFriendRequestCreateDto(senderDto, receiverDto));
        Assertions.assertThrows(EventorException.class,
                () -> friendRequestController.createRequest(validFriendRequestCreateDto(senderDto, receiverDto))
        );
    }
}
