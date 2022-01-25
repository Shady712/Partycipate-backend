package com.sasd.eventor.friendrequest;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.FriendRequestResponseDto;
import com.sasd.eventor.model.enums.RequestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.model.enums.RequestStatus.ACCEPTED;
import static com.sasd.eventor.model.enums.RequestStatus.REJECTED;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class FriendRequestUpdateTest extends FriendRequestTest {

    @Test
    public void acceptValidRequest() {
        var request = abstractHappyPathTest(
                ((id, receiverJwt) -> friendRequestController.acceptRequest(id, receiverJwt)),
                ACCEPTED
        );
        assert userController.findAllFriends(request.getSenderLogin()).stream()
                .anyMatch(userResponseDto -> userResponseDto.getLogin().equals(request.getReceiverLogin()));
        assert userController.findAllFriends(request.getReceiverLogin()).stream()
                .anyMatch(userResponseDto -> userResponseDto.getLogin().equals(request.getSenderLogin()));
    }

    @Test
    public void rejectValidRequest() {
        abstractHappyPathTest(((id, receiverJwt) -> friendRequestController.rejectRequest(id, receiverJwt)), REJECTED);
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        var receiverDto = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(validUserRegisterDto(), receiverDto)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.acceptRequest(request.getId() + 100, getJwt(receiverDto))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.rejectRequest(request.getId() + 100, getJwt(receiverDto))
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        var request = friendRequestController.createRequest(validFriendRequestCreateDto());
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.acceptRequest(request.getId(), "Invalid jwt")
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.rejectRequest(request.getId(), "Invalid jwt")
        );
    }

    @Test
    public void ensureBadRequestForInvalidReceiver() {
        var request = friendRequestController.createRequest(validFriendRequestCreateDto());
        var invalidReceiverDto = validUserRegisterDto();
        userController.register(invalidReceiverDto);
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.acceptRequest(request.getId(), getJwt(invalidReceiverDto))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.rejectRequest(request.getId(), getJwt(invalidReceiverDto))
        );
    }

    @Test
    public void ensureBadRequestForInvalidStatus() {
        var receiverDto = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(validUserRegisterDto(), receiverDto)
        );
        friendRequestController.acceptRequest(request.getId(), getJwt(receiverDto));
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.acceptRequest(request.getId(), getJwt(receiverDto))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.rejectRequest(request.getId(), getJwt(receiverDto))
        );
    }

    private FriendRequestResponseDto abstractHappyPathTest(RequestHandler requestHandler, RequestStatus status) {
        var receiverDto = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(validUserRegisterDto(), receiverDto)
        );
        var updatedRequest = requestHandler.handleRequest(request.getId(), getJwt(receiverDto));

        assert request.getId().equals(updatedRequest.getId());
        assert request.getSenderLogin().equals(updatedRequest.getSenderLogin());
        assert request.getReceiverLogin().equals(updatedRequest.getReceiverLogin());
        assert updatedRequest.getStatus().equals(status);

        return updatedRequest;
    }

    private interface RequestHandler {
        FriendRequestResponseDto handleRequest(Long id, String receiverJwt);
    }
}
