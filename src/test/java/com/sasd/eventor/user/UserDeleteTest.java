package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserDeleteTest extends UserTest {

    @Test
    public void ensureBadRequestForFindingDeletedUser() {
        var registerUser = userController.register(validUserRegisterDto());
        userController.deleteById(registerUser.getId(), getJwt());
        Assertions.assertThrows(EventorException.class, () -> userController.findById(registerUser.getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUnregisteredUser() {
        var registerUser = userController.register(validUserRegisterDto());
        Assertions.assertThrows(EventorException.class,
                () -> userController.deleteById(registerUser.getId() + 100, getJwt()));
    }

    private String getJwt() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        return userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
    }
}