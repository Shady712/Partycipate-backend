package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.daos.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sasd.eventor.utils.UserUtils.*;

public class UserRegistrationTest extends UserTest {

    @Test
    public void registerValidUser(@Autowired UserRepository userRepository) {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        var foundUser = userRepository
                .findAll()
                .stream()
                .filter(user -> user.getLogin().equals(userRegisterDto.getLogin()))
                .findFirst();
        assert foundUser.isPresent();
        var user = foundUser.get();
        assert user.getLogin().equals(userRegisterDto.getLogin());
        assert user.getName().equals(userRegisterDto.getName());
    }

    @Test
    public void ensureBadRequestForUsedLogin() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        userRegisterDto.setName(validUserName());
        userRegisterDto.setPassword(validUserPassword());
        Assertions.assertThrows(EventorException.class, () ->
                userController.register(userRegisterDto)
        );
    }
}
