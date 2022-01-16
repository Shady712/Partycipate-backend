package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserUpdateTest extends UserTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void updateWithChanges() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var jwt = userController.createJwt(dto.getLogin(), dto.getPassword());
        var updateDto = new UserUpdateDto();
        updateDto.setLogin(VALID_LOGIN + 'q');
        updateDto.setName(VALID_NAME + 'q');
        updateDto.setPassword(VALID_PASSWORD + 'q');
        var updatedUser = userController.update(updateDto, jwt);
        assert updatedUser.getLogin().equals(VALID_LOGIN + 'q');
        assert updatedUser.getName().equals(VALID_NAME + 'q');
    }

    @Test
    public void updateWithoutChanges() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var jwt = userController.createJwt(dto.getLogin(), dto.getPassword());
        var updateDto = new UserUpdateDto();
        updateDto.setLogin(VALID_LOGIN);
        updateDto.setName(VALID_NAME);
        updateDto.setPassword(VALID_PASSWORD);
        var updatedUser = userController.update(updateDto, jwt);
        assert updatedUser.getLogin().equals(dto.getLogin());
        assert updatedUser.getName().equals(dto.getName());
    }

    @Test
    public void ensureBadRequestForInvalidLoginOrPassword() {
        userController.register(validUserRegisterDto());
        Assertions.assertThrows(EventorException.class,
                () -> userController.createJwt(VALID_LOGIN + VALID_LOGIN, VALID_PASSWORD + VALID_PASSWORD)
        );
    }
}