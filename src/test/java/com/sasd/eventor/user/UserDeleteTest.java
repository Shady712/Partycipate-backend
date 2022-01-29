package com.sasd.eventor.user;

import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserResponseDto;
import com.sasd.eventor.model.dtos.FriendRequestCreateDto;
import com.sasd.eventor.utils.EventUtils;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserDeleteTest extends UserTest {
    @Autowired
    private EventController eventController;

    @Test
    public void successfulDeletingWithExistingIncomingAndOutcomingFriendRequests() {
        var userDtoWithJwt = new UserDtoWithJwt();
        createOutcomingFriendRequest(userDtoWithJwt.getRegisteredUserJwt());
        createIncomingFriendRequest(userDtoWithJwt.getRegisteredUser().getLogin());
        userController.delete(userDtoWithJwt.getRegisteredUser().getId(), userDtoWithJwt.getRegisteredUserJwt());
        Assertions.assertThrows(EventorException.class,
                () -> userController.findById(userDtoWithJwt.getRegisteredUser().getId()));
    }

    @Test
    public void ensureBadRequestForDeniedPermission() {
        var firstUserDtoWithJwt = new UserDtoWithJwt();
        var secondUserDtoWithJwt = new UserDtoWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> userController.delete(firstUserDtoWithJwt.getRegisteredUser().getId(),
                        secondUserDtoWithJwt.getRegisteredUserJwt()));
    }

    @Test
    public void ensureBadRequestForDeletingUnregisteredUser() {
        var userDtoWithJwt = new UserDtoWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> userController.delete(userDtoWithJwt.getRegisteredUser().getId() + 100,
                        userDtoWithJwt.getRegisteredUserJwt()));
    }

    @Test
    public void ensureBadRequestForDeletingWithOpenEvent() {
        var userDtoWithJwt = new UserDtoWithJwt();
        createValidEvent(userDtoWithJwt.getRegisteredUserJwt());
        Assertions.assertThrows(EventorException.class,
                () -> userController.delete(userDtoWithJwt.getRegisteredUser().getId() + 100,
                        userDtoWithJwt.getRegisteredUserJwt()));
    }

    private void createValidEvent(String creatorJwt) {
        var eventDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventDto.setJwt(creatorJwt);
        eventController.create(eventDto);
    }

    private void createOutcomingFriendRequest(String senderJwt) {
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