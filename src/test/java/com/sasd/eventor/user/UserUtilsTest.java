package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertDoesNotThrow(() -> userController.requestPasswordChange(registerUser().getLogin()));
        Assertions.assertDoesNotThrow(() -> userController.requestPasswordChange(registerUser().getEmail()));
    }

    @Test
    public void ensureBadRequestForRequestPasswordChangeWithUnverifiedEmail() {
        Assertions.assertThrows(
                EventorException.class,
                () -> userController.requestPasswordChange(userController.register(validUserRegisterDto()).getLogin())
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> userController.requestPasswordChange(userController.register(validUserRegisterDto()).getEmail())
        );
    }
}
