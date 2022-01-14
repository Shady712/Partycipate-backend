package com.sasd.eventor.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserUtilsTest extends UserTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void checkExistingUserLoginVacancy() {
        assert !userController.isLoginVacant(userController.register(validUserRegisterDto()).getLogin());
    }

    @Test
    public void checkNonExistingUserLoginVacancy() {
        assert userController.isLoginVacant("VacantLogin");
    }
}
