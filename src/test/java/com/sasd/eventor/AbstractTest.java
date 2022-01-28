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

    protected String getJwt() {
        return getJwt(validUserRegisterDto());
    }

    protected UserResponseDto registerUser(UserRegisterDto dto) {
        if (userController.isLoginVacant(dto.getLogin())) {
            return userController.register(dto);
        } else {
            return userController.findByLogin(dto.getLogin());
        }
    }

    protected String getJwt(UserRegisterDto dto) {
        registerUser(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    protected UserResponseDto registerUser() {
        return registerUser(validUserRegisterDto());
    }
}
