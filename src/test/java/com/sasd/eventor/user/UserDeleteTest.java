package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserResponseDto;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserDeleteTest extends UserTest {

    @Test
    public void successfulDeleting() {
        var userDtoWithJwt = new UserDtoWithJwt();
        userController.deleteById(userDtoWithJwt.getRegisteredUser().getId(), userDtoWithJwt.getRegisteredUserJwt());
        Assertions.assertThrows(EventorException.class,
                () -> userController.findById(userDtoWithJwt.getRegisteredUser().getId()));
    }

    @Test
    public void ensureBadRequestForDeniedPermission() {
        var firstUserDtoWithJwt = new UserDtoWithJwt();
        var secondUserDtoWithJwt = new UserDtoWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> userController.deleteById(firstUserDtoWithJwt.getRegisteredUser().getId(),
                        secondUserDtoWithJwt.getRegisteredUserJwt()));
    }

    @Test
    public void ensureBadRequestForDeletingUnregisteredUser() {
        var userDtoWithJwt = new UserDtoWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> userController.deleteById(userDtoWithJwt.getRegisteredUser().getId() + 100,
                        userDtoWithJwt.getRegisteredUserJwt()));
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