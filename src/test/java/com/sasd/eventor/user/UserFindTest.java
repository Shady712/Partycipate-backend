package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserResponseDto;
import com.sasd.eventor.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

public class UserFindTest extends UserTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

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
    public void ensureBadRequestForInvalidLogin() {
        userController.register(validUserRegisterDto());
        Assertions.assertThrows(EventorException.class,
                () -> userController.createJwt(VALID_LOGIN + VALID_LOGIN, VALID_PASSWORD + VALID_PASSWORD)
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwtToken() {
        Assertions.assertThrows(EventorException.class, () -> userController.enterByJwt("invalid jwt"));
    }

    private UserResponseDto registerValidUser() {
        return userController.register(validUserRegisterDto());
    }
}
