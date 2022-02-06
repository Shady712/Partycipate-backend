package com.partycipate.user;

import com.partycipate.exception.PartycipateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.partycipate.utils.UserUtils.*;

public class UserEnterTest extends UserTest {

    @Test
    public void enterByLoginAndPassword() {
        var dto = validUserRegisterDto();
        registerUser(dto);
        var jwt = userController.createJwt(dto.getLogin(), dto.getPassword());
        var enteredUser = userController.enterByJwt(jwt);
        assert dto.getLogin().equals(enteredUser.getLogin());
        assert dto.getName().equals(enteredUser.getName());
    }

    @Test
    public void ensureBadRequestForInvalidLoginOrPassword() {
        userController.register(validUserRegisterDto());
        Assertions.assertThrows(PartycipateException.class,
                () -> userController.createJwt(validUserLogin(), validUserPassword())
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwtToken() {
        Assertions.assertThrows(PartycipateException.class, () -> userController.enterByJwt("invalid jwt"));
    }

    @Test
    public void ensureBadRequestForUnverifiedEmail() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        Assertions.assertThrows(
                PartycipateException.class,
                () -> userController.createJwt(dto.getLogin(), dto.getPassword())
        );
    }
}
