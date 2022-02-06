package com.partycipate.user;

import com.partycipate.utils.EventUtils;
import com.partycipate.controllers.EventController;
import com.partycipate.exception.PartycipateException;
import com.partycipate.model.dtos.UserResponseDto;
import com.partycipate.model.dtos.FriendRequestCreateDto;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.partycipate.utils.EventUtils.validEventCreateDtoWithoutJwt;
import static com.partycipate.utils.UserUtils.validUserRegisterDto;

public class UserDeleteTest extends UserTest {
    @Autowired
    private EventController eventController;

    @Test
    public void successfulDeletingWithExistingIncomingAndOutgoingFriendRequests() {
        var userDtoWithJwt = new UserDtoWithJwt();
        createOutgoingFriendRequest(userDtoWithJwt.getRegisteredUserJwt());
        createIncomingFriendRequest(userDtoWithJwt.getRegisteredUser().getLogin());
        userController.delete(userDtoWithJwt.getRegisteredUserJwt());
        Assertions.assertThrows(
                PartycipateException.class,
                () -> userController.findById(userDtoWithJwt.getRegisteredUser().getId())
        );
    }

    @Test
    public void ensureBadRequestForDeletingWithOpenEvent() {
        var userDtoWithJwt = new UserDtoWithJwt();
        createValidEvent(userDtoWithJwt.getRegisteredUserJwt());
        Assertions.assertThrows(
                PartycipateException.class,
                () -> userController.delete(userDtoWithJwt.getRegisteredUserJwt())
        );
    }

    private void createValidEvent(String creatorJwt) {
        var eventDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventDto.setJwt(creatorJwt);
        eventController.create(eventDto);
    }

    private void createOutgoingFriendRequest(String senderJwt) {
        createFriendRequest(userController.register(validUserRegisterDto()).getLogin(), senderJwt);
    }

    private void createIncomingFriendRequest(String receiverLogin) {
        var sender = validUserRegisterDto();
        registerUser(sender);
        createFriendRequest(receiverLogin, userController.createJwt(sender.getLogin(), sender.getPassword()));
    }

    private void createFriendRequest(String receiverLogin, String senderJwt) {
        var requestCreateDto = new FriendRequestCreateDto();
        requestCreateDto.setSenderJwt(senderJwt);
        requestCreateDto.setReceiverLogin(receiverLogin);
        friendRequestController.createRequest(requestCreateDto);
    }

    @Getter
    private class UserDtoWithJwt {
        private final UserResponseDto registeredUser;
        private final String registeredUserJwt;

        private UserDtoWithJwt() {
            var userRegisterDto = validUserRegisterDto();
            registeredUser = registerUser(userRegisterDto);
            registeredUserJwt = userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
        }
    }
}