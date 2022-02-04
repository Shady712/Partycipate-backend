package com.sasd.eventor.user;

import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.dtos.UserUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class UserUpdateTest extends UserTest {

    @Test
    public void updateWithChanges() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var updateDto = userUpdateDto(userController.createJwt(dto.getLogin(), dto.getPassword()),
                dto.getLogin() + 'q', dto.getName() + 'q');
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin() + 'q');
        assert updatedUser.getName().equals(dto.getName() + 'q');
    }

    @Test
    public void updateWithOneChange() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var updateDto = userUpdateDto(userController.createJwt(dto.getLogin(), dto.getPassword()),
                dto.getLogin() + 'q', dto.getName());
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin() + 'q');
        assert updatedUser.getName().equals(dto.getName());
    }

    @Test
    public void updateWithoutChanges() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var updateDto = userUpdateDto(userController.createJwt(dto.getLogin(), dto.getPassword()),
                dto.getLogin(), dto.getName());
        var updatedUser = userController.update(updateDto);
        assert updatedUser.getLogin().equals(dto.getLogin());
        assert updatedUser.getName().equals(dto.getName());
    }

    @Test
    public void changePassword(@Autowired UserRepository userRepository) {
        var user = userRepository.findById(registerUser().getId()).orElseThrow();
        var response = userController.changePassword(
                user.getLogin(),
                user.getPasswordHash(),
                "new password"
        );
        assert user.getLogin().equals(response.getLogin());
        assert user.getName().equals(response.getName());
        assert user.getEmail().equals(response.getEmail());
        assert !user.getPasswordHash().equals(userRepository.findById(user.getId()).orElseThrow().getPasswordHash());
    }

    private UserUpdateDto userUpdateDto(String jwt, String newLogin, String newName) {
        var userUpdateDto = new UserUpdateDto();
        userUpdateDto.setJwt(jwt);
        userUpdateDto.setLogin(newLogin);
        userUpdateDto.setName(newName);
        return userUpdateDto;
    }
}