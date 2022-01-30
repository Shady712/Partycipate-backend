package com.sasd.eventor;

import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.dtos.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractTest {
    @Autowired
    protected UserController userController;

    protected String getJwt() {
        return getJwt(validUserRegisterDto());
    }

    protected String getJwt(UserRegisterDto dto) {
        registerUser(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    protected UserResponseDto registerUser() {
        return registerUser(validUserRegisterDto());
    }

    protected UserResponseDto registerUser(UserRegisterDto dto) {
        try {
            return userController.findByLogin(dto.getLogin());
        } catch (EventorException ignored) {
            return userController.register(dto);
        }
    }
}
