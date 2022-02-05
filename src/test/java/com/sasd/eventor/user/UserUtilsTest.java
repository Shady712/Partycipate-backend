package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.sasd.eventor.utils.UserUtils.validUserLogin;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

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
                EventorException.class,
                () -> userController.requestPasswordChange(loginOrEmail)
        ));
    }
}
