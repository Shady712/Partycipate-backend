package com.sasd.eventor.user;

import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserEnterTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;

    private static final String VALID_LOGIN = "Valid12345";
    private static final String VALID_NAME = "Valid Name";
    private static final String VALID_PASSWORD = "QWErty12345";

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
    }

    @Test
    public void enterByLoginAndPassword() {
        userController.register(validUserRegisterDto());
        var enterUser = userController.enter(VALID_LOGIN, VALID_PASSWORD);
        assert enterUser.getLogin().equals(VALID_LOGIN);
        assert enterUser.getPassword().equals(VALID_PASSWORD);
    }

    @Test
    public void ensureBadRequestForInvalidLoginOrPassword() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        Assertions.assertThrows(EventorException.class, () -> userController.enter(VALID_LOGIN + VALID_LOGIN,
                VALID_PASSWORD + VALID_PASSWORD));

    }

    private UserRegisterDto validUserRegisterDto() {
        var dto = new UserRegisterDto();
        dto.setLogin(VALID_LOGIN);
        dto.setName(VALID_NAME);
        dto.setPassword(VALID_PASSWORD);
        return dto;
    }
}
