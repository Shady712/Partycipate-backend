package com.sasd.eventor.friendrequest;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class FriendRequestDeleteTest extends FriendRequestTest {

    @Test
    public void deleteExistingRequest() {
        var sender = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(sender, validUserRegisterDto())
        );
        friendRequestController.deleteRequest(request.getId(), getJwt(sender));
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.findById(request.getId())
        );
    }

    @Test
    public void ensureBadRequestOnDeleteNonExistingRequest() {
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.deleteRequest(Long.MAX_VALUE, getJwt(validUserRegisterDto()))
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByInvalidJwt() {
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.deleteRequest(
                        friendRequestController.createRequest(validFriendRequestCreateDto()).getId(),
                        "Invalid jwt"
                )
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByOtherSender() {
        Assertions.assertThrows(
                EventorException.class,
                () -> friendRequestController.deleteRequest(
                        friendRequestController.createRequest(validFriendRequestCreateDto()).getId(),
                        getJwt(validUserRegisterDto())
                )
        );
    }
}
