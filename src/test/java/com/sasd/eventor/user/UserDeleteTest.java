package com.sasd.eventor.user;

import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserResponseDto;
import com.sasd.eventor.model.dtos.FriendRequestCreateDto;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sasd.eventor.utils.EventUtils.validEventCreateDtoWithoutJwt;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

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
                EventorException.class,
                () -> userController.findById(userDtoWithJwt.getRegisteredUser().getId())
        );
    }

    @Test
    public void ensureBadRequestForDeletingWithOpenEvent() {
        var userDtoWithJwt = new UserDtoWithJwt();
        createValidEvent(userDtoWithJwt.getRegisteredUserJwt());
        Assertions.assertThrows(
                EventorException.class,
                () -> userController.delete(userDtoWithJwt.getRegisteredUserJwt())
        );
    }

    private void createValidEvent(String creatorJwt) {
        var eventDto = validEventCreateDtoWithoutJwt();
        eventDto.setJwt(creatorJwt);
        eventController.create(eventDto);
    }

    private void createOutgoingFriendRequest(String senderJwt) {
        createFriendRequest(userController.register(validUserRegisterDto()).getLogin(), senderJwt);
    }

    private void createIncomingFriendRequest(String receiverLogin) {
        var sender = validUserRegisterDto();
        userController.register(sender);
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
            registeredUser = userController.register(userRegisterDto);
            registeredUserJwt = userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
        }
    }
}