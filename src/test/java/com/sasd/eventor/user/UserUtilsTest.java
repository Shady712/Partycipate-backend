package com.sasd.eventor.user;

import com.sasd.eventor.model.dtos.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserUtilsTest extends UserTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void checkExistingUserLoginVacancy() {
        var user = userController.register(validUserRegisterDto());
        assert !userController.isLoginVacant(user.getLogin());
    }

    @Test
    public void checkNonExistingUserLoginVacancy() {
        assert userController.isLoginVacant("VacantLogin");
    }
}
