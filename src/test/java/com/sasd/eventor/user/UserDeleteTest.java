package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserDeleteTest extends UserTest {

    @Test
    public void ensureBadRequestForDeniedPermission() {
        var userRegisterDto = validUserRegisterDto();
        var registerUser = userController.register(userRegisterDto);
        var jwt = userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
        var newUserRegisterDto = validUserRegisterDto();
        userController.register(newUserRegisterDto);
        var invalidJwt = userController.createJwt(newUserRegisterDto.getLogin(), newUserRegisterDto.getPassword());
        userController.deleteById(registerUser.getId(), jwt);
        Assertions.assertThrows(EventorException.class,
                () -> userController.deleteById(registerUser.getId(), invalidJwt));
    }

    @Test
    public void ensureBadRequestForFindingDeletedUser() {
        var userRegisterDto = validUserRegisterDto();
        var registerUser = userController.register(userRegisterDto);
        var jwt = userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
        userController.deleteById(registerUser.getId(), jwt);
        Assertions.assertThrows(EventorException.class, () -> userController.findById(registerUser.getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUnregisteredUser() {
        var userRegisterDto = validUserRegisterDto();
        var registerUser = userController.register(userRegisterDto);
        var jwt = userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
        Assertions.assertThrows(EventorException.class,
                () -> userController.deleteById(registerUser.getId() + 100, jwt));
    }
}