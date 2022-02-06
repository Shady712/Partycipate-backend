package com.partycipate.user;

import com.partycipate.exception.PartycipateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.partycipate.utils.UserUtils.validUserLogin;
import static com.partycipate.utils.UserUtils.validUserRegisterDto;

public class UserUtilsTest extends UserTest {

    @Test
    public void checkExistingUserLoginVacancy() {
        assert !userController.isLoginVacant(userController.register(validUserRegisterDto()).getLogin());
    }

    @Test
    public void checkNonExistingUserLoginVacancy() {
        assert userController.isLoginVacant(validUserLogin());
    }

    @Test
    public void requestChangePassword() {
        Stream.of(
                registerUser().getLogin(),
                registerUser().getEmail()
        ).forEach(loginOrEmail -> Assertions.assertDoesNotThrow(() -> userController.requestPasswordChange(loginOrEmail)));
    }

    @Test
    public void ensureBadRequestForRequestPasswordChangeWithUnverifiedEmail() {
        Stream.of(
                userController.register(validUserRegisterDto()).getLogin(),
                userController.register(validUserRegisterDto()).getEmail()
        ).forEach(loginOrEmail -> Assertions.assertThrows(
                PartycipateException.class,
                () -> userController.requestPasswordChange(loginOrEmail)
        ));
    }
}
