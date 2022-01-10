package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserRegistrationTest extends UserTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void registerValidUser() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        var foundUser = userRepository
                .findAll()
                .stream()
                .filter(user -> user.getLogin().equals(VALID_LOGIN))
                .findFirst();
        assert foundUser.isPresent();
        var user = foundUser.get();
        assert user.getLogin().equals(VALID_LOGIN);
        assert user.getName().equals(VALID_NAME);
        assert user.getPassword().equals(VALID_PASSWORD);
    }

    @Test
    public void ensureBadRequestForUsedLogin() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        userRegisterDto.setName(VALID_NAME + VALID_NAME);
        userRegisterDto.setPassword(VALID_PASSWORD + VALID_PASSWORD);
        Assertions.assertThrows(EventorException.class, () ->
                userController.register(userRegisterDto)
        );
    }
}
