package com.sasd.eventor;

import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.dtos.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

@SpringBootTest
public abstract class AbstractTest {
    @Autowired
    protected UserController userController;

    protected String validJwt() {
        return validJwt(validUserRegisterDto());
    }

    protected String validJwt(UserRegisterDto dto) {
        userController.register(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    protected void ensureUserRegistration(UserRegisterDto dto) {
        if (userController.isLoginVacant(dto.getLogin())) {
            userController.register(dto);
        }
    }

    protected String getJwt(UserRegisterDto dto) {
        ensureUserRegistration(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    protected UserResponseDto registerUser() {
        return registerUser(validUserRegisterDto());
    }

    protected UserResponseDto registerUser(UserRegisterDto dto) {
        return userController.register(dto);
    }
}
