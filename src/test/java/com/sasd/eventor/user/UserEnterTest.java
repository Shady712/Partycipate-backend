package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserEnterTest extends UserTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void enterByLoginAndPassword() {
        userController.register(validUserRegisterDto());
        var enterUser = userController.enter(VALID_LOGIN, VALID_PASSWORD);
        assert enterUser.getLogin().equals(VALID_LOGIN);
        assert enterUser.getPassword().equals(VALID_PASSWORD);
    }

    @Test
    public void ensureBadRequestForInvalidLoginOrPassword() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        Assertions.assertThrows(EventorException.class, () -> userController.enter(VALID_LOGIN + VALID_LOGIN,
                VALID_PASSWORD + VALID_PASSWORD));

    }
}
