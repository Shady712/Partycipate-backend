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
        abstractAcceptRequestTest((id, receiverJwt) -> friendRequestController.acceptRequest(id, receiverJwt));
    }

    @Test
    public void acceptRejectedRequest() {
        abstractAcceptRequestTest((id, receiverJwt) -> {
            friendRequestController.rejectRequest(id, receiverJwt);
            return friendRequestController.acceptRequest(id, receiverJwt);
        });
    }

    @Test
    public void rejectValidRequest() {
        abstractHappyPathTest(((id, receiverJwt) -> friendRequestController.rejectRequest(id, receiverJwt)), REJECTED);
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        var receiverDto = validUserRegisterDto();
        friendRequestController.createRequest(
                validFriendRequestCreateDto(validUserRegisterDto(), receiverDto)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.acceptRequest(Long.MAX_VALUE, getJwt(receiverDto))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.rejectRequest(Long.MAX_VALUE, getJwt(receiverDto))
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
        var rejectedRequest = friendRequestController.createRequest(
                validFriendRequestCreateDto(validUserRegisterDto(), receiverDto)
        );
        var receiverJwt = getJwt(receiverDto);
        friendRequestController.rejectRequest(rejectedRequest.getId(), receiverJwt);
        friendRequestController.acceptRequest(request.getId(), receiverJwt);

        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.acceptRequest(request.getId(), receiverJwt)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.rejectRequest(request.getId(), receiverJwt)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.rejectRequest(rejectedRequest.getId(), receiverJwt)
        );
    }

    private void abstractAcceptRequestTest(RequestHandler requestHandler) {
        var request = abstractHappyPathTest(
                requestHandler,
                ACCEPTED
        );
        assert userController.findAllFriends(request.getSender().getLogin()).stream()
                .anyMatch(userResponseDto -> userResponseDto.equals(request.getReceiver()));
        assert userController.findAllFriends(request.getReceiver().getLogin()).stream()
                .anyMatch(userResponseDto -> userResponseDto.equals(request.getSender()));
    }

    private FriendRequestResponseDto abstractHappyPathTest(RequestHandler requestHandler, RequestStatus status) {
        var receiverDto = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(validUserRegisterDto(), receiverDto)
        );
        var updatedRequest = requestHandler.handleRequest(request.getId(), getJwt(receiverDto));

        assert updatedRequest.getId().equals(request.getId());
        assert updatedRequest.getSender().equals(request.getSender());
        assert updatedRequest.getReceiver().equals(request.getReceiver());
        assert updatedRequest.getStatus().equals(status);

        return updatedRequest;
    }

    private interface RequestHandler {
        FriendRequestResponseDto handleRequest(Long id, String receiverJwt);
    }
}
