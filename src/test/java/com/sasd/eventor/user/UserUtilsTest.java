package com.sasd.eventor.user;

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
}
