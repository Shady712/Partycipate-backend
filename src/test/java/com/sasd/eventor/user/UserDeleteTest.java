package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserDeleteTest extends UserTest {

    @Test
    public void ensureBadRequestForFindingDeletedUser() {
        var registerUser = userController.register(validUserRegisterDto());
        userController.deleteById(registerUser.getId());
        Assertions.assertThrows(EventorException.class, () -> userController.findById(registerUser.getId()));
    }
}