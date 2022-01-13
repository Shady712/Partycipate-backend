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
        var dto = validUserRegisterDto();
        userController.register(dto);
        var jwt = userController.createJwt(dto.getLogin(), dto.getPassword());
        var enteredUser = userController.enterByJwt(jwt);
        assert dto.getLogin().equals(enteredUser.getLogin());
        assert dto.getName().equals(enteredUser.getName());
    }

    @Test
    public void ensureBadRequestForInvalidLoginOrPassword() {
        userController.register(validUserRegisterDto());
        Assertions.assertThrows(EventorException.class,
                () -> userController.createJwt(VALID_LOGIN + VALID_LOGIN, VALID_PASSWORD + VALID_PASSWORD)
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwtToken() {
        Assertions.assertThrows(EventorException.class, () -> userController.enterByJwt("invalid jwt"));
    }
}
