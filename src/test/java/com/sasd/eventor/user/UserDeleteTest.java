package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserDeleteTest extends UserTest {

    @Test
    public void ensureBadRequestForDeniedPermission() {
        var firstUserData = new registeredUserData();
        var secondUserData = new registeredUserData();
        Assertions.assertThrows(EventorException.class,
                () -> userController.deleteById(firstUserData.getRegisteredUser().getId(),
                        secondUserData.getRegisteredUserJwt()));
    }

    @Test
    public void ensureBadRequestForFindingDeletedUser() {
        var registeredUserData = new registeredUserData();
        userController.deleteById(registeredUserData.getRegisteredUser().getId(), registeredUserData.getRegisteredUserJwt());
        Assertions.assertThrows(EventorException.class,
                () -> userController.findById(registeredUserData.getRegisteredUser().getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUnregisteredUser() {
        var registeredUserData = new registeredUserData();
        Assertions.assertThrows(EventorException.class,
                () -> userController.deleteById(registeredUserData.getRegisteredUser().getId() + 100,
                        registeredUserData.getRegisteredUserJwt()));
    }

    private class registeredUserData {
        private final UserResponseDto registeredUser;
        private final String registeredUserJwt;

        private registeredUserData() {
            var userRegisterDto = validUserRegisterDto();
            registeredUser = userController.register(userRegisterDto);
            registeredUserJwt = userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
        }

        public UserResponseDto getRegisteredUser() {
            return registeredUser;
        }

        public String getRegisteredUserJwt() {
            return registeredUserJwt;
        }
    }
}