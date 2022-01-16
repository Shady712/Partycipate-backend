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
        var updateDto = makeUserUpdateDto(userController.createJwt(dto.getLogin(), dto.getPassword()),
                dto.getLogin() + 'q', dto.getName() + 'q');
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin() + 'q');
        assert updatedUser.getName().equals(dto.getName() + 'q');
    }

    @Test
    public void updateWithOneChange() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var updateDto = makeUserUpdateDto(userController.createJwt(dto.getLogin(), dto.getPassword()),
                dto.getLogin() + 'q', dto.getName());
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin() + 'q');
        assert updatedUser.getName().equals(dto.getName());
    }

    @Test
    public void updateWithoutChanges() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var updateDto = makeUserUpdateDto(userController.createJwt(dto.getLogin(), dto.getPassword()),
                dto.getLogin(), dto.getName());
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin());
        assert updatedUser.getName().equals(dto.getName());
    }

    private UserUpdateDto makeUserUpdateDto(String jwt, String newLogin, String newName) {
        var userUpdateDto = new UserUpdateDto();
        userUpdateDto.setJwt(jwt);
        userUpdateDto.setLogin(newLogin);
        userUpdateDto.setName(newName);
        return userUpdateDto;
    }
}