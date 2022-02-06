package com.partycipate.user;

import com.partycipate.exception.PartycipateException;
import com.partycipate.model.daos.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.partycipate.utils.UserUtils.*;

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
        Assertions.assertThrows(PartycipateException.class, () ->
                userController.register(userRegisterDto)
        );
    }
}
