package com.sasd.eventor.user;

import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class UserTest {
    @Autowired
    protected UserController userController;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserRepository userService;

    protected static final String VALID_LOGIN = "Valid12345";
    protected static final String VALID_NAME = "Valid Name";
    protected static final String VALID_PASSWORD = "QWErty12345";

    protected void clearDb() {
        userRepository.deleteAll();
    }

    protected static UserRegisterDto validUserRegisterDto() {
        var dto = new UserRegisterDto();
        dto.setLogin(VALID_LOGIN);
        dto.setName(VALID_NAME);
        dto.setPassword(VALID_PASSWORD);
        return dto;
    }
}
