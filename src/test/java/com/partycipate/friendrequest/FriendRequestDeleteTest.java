package com.partycipate.friendrequest;

import com.partycipate.exception.PartycipateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.partycipate.utils.UserUtils.validUserRegisterDto;

public class FriendRequestDeleteTest extends FriendRequestTest {

    @Test
    public void deleteExistingRequest() {
        var sender = validUserRegisterDto();
        var request = friendRequestController.createRequest(
                validFriendRequestCreateDto(sender, validUserRegisterDto())
        );
        friendRequestController.deleteRequest(request.getId(), getJwt(sender));
        Assertions.assertThrows(
                PartycipateException.class,
                () -> friendRequestController.findById(request.getId())
        );
    }

    @Test
    public void ensureBadRequestOnDeleteNonExistingRequest() {
        Assertions.assertThrows(
                PartycipateException.class,
                () -> friendRequestController.deleteRequest(Long.MAX_VALUE, getJwt())
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByInvalidJwt() {
        Assertions.assertThrows(
                PartycipateException.class,
                () -> friendRequestController.deleteRequest(
                        friendRequestController.createRequest(validFriendRequestCreateDto()).getId(),
                        "Invalid jwt"
                )
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByOtherSender() {
        Assertions.assertThrows(
                PartycipateException.class,
                () -> friendRequestController.deleteRequest(
                        friendRequestController.createRequest(validFriendRequestCreateDto()).getId(),
                        getJwt(validUserRegisterDto())
                )
        );
    }
}
