package com.sasd.eventor.user;

import com.sasd.eventor.model.dtos.UserUpdateDto;
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
        updateDto.setJwt(jwt);
        updateDto.setLogin(VALID_LOGIN + 'q');
        updateDto.setName(VALID_NAME + 'q');
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(VALID_LOGIN + 'q');
        assert updatedUser.getName().equals(VALID_NAME + 'q');
    }

    @Test
    public void updateWithOneChange() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var jwt = userController.createJwt(dto.getLogin(), dto.getPassword());
        var updateDto = new UserUpdateDto();
        updateDto.setJwt(jwt);
        updateDto.setLogin(VALID_LOGIN + 'q');
        updateDto.setName(VALID_NAME);
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin()+'q');
        assert updatedUser.getName().equals(dto.getName());
    }

    @Test
    public void updateWithoutChanges() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var jwt = userController.createJwt(dto.getLogin(), dto.getPassword());
        var updateDto = new UserUpdateDto();
        updateDto.setJwt(jwt);
        updateDto.setLogin(VALID_LOGIN);
        updateDto.setName(VALID_NAME);
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin());
        assert updatedUser.getName().equals(dto.getName());
    }
}