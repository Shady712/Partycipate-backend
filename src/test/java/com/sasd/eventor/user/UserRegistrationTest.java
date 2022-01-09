package com.sasd.eventor.user;

import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRegistrationTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;

    private static final String VALID_LOGIN = "Valid12345";
    private static final String VALID_NAME = "Valid Name";
    private static final String VALID_PASSWORD = "QWErty12345";

    @Test
    public void registerValidUser() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        var foundUser = userRepository
                .findAll()
                .stream()
                .filter(user -> user.getLogin().equals(VALID_LOGIN))
                .findFirst();
        assert foundUser.isPresent();
        var user = foundUser.get();
        assert user.getLogin().equals(VALID_LOGIN);
        assert user.getName().equals(VALID_NAME);
        assert user.getPassword().equals(VALID_PASSWORD);
    }

    @Test
    public void ensureBadRequestForUsedLogin() {
        // TODO
    }

    private UserRegisterDto validUserRegisterDto() {
        var dto = new UserRegisterDto();
        dto.setLogin(VALID_LOGIN);
        dto.setName(VALID_NAME);
        dto.setPassword(VALID_PASSWORD);
        return dto;
    }
}
