package com.partycipate.friendrequest;

import com.partycipate.exception.PartycipateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.partycipate.model.enums.RequestStatus.ACCEPTED;
import static com.partycipate.utils.UserUtils.validUserRegisterDto;

public class FriendRequestCreateTest extends FriendRequestTest {

    @Test
    public void createValidFriendRequest() {
        var senderDto = validUserRegisterDto();
        var receiverDto = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(senderDto, receiverDto)
        );
        assert request.getSender().getLogin().equals(senderDto.getLogin());
        assert request.getReceiver().getLogin().equals(receiverDto.getLogin());
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
        Assertions.assertThrows(PartycipateException.class, () -> friendRequestController.createRequest(dto));
    }

    @Test
    public void ensureBadRequestForInvalidLogin() {
        var dto = validFriendRequestCreateDto();
        dto.setReceiverLogin("Invalid login");
        Assertions.assertThrows(PartycipateException.class, () -> friendRequestController.createRequest(dto));
    }

    @Test
    public void ensureBadRequestForRepeatedRequest() {
        var senderDto = validUserRegisterDto();
        var receiverDto = validUserRegisterDto();
        friendRequestController.createRequest(validFriendRequestCreateDto(senderDto, receiverDto));
        Assertions.assertThrows(
                PartycipateException.class,
                () -> friendRequestController.createRequest(validFriendRequestCreateDto(senderDto, receiverDto))
        );
    }
}
