package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserFindTest extends UserTest {

    @Test
    public void findExistingUserById() {
        var expectedUser = registerValidUser();
        var foundUser = userController.findById(expectedUser.getId());
        assert expectedUser.getLogin().equals(foundUser.getLogin());
        assert expectedUser.getName().equals(foundUser.getName());
    }

    @Test
    public void findExistingUserByLogin() {
        var expectedUser = registerValidUser();
        var foundUser = userController.findByLogin(expectedUser.getLogin());
        assert expectedUser.getLogin().equals(foundUser.getLogin());
        assert expectedUser.getName().equals(foundUser.getName());
    }

    @Test
    public void ensureBadRequestForInvalidJwtToken() {
        Assertions.assertThrows(EventorException.class, () -> userController.enterByJwt("invalid jwt"));
    }

    private UserResponseDto registerValidUser() {
        return userController.register(validUserRegisterDto());
    }
}
