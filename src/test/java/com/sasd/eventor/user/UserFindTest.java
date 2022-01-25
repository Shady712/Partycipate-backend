package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.*;

public class UserFindTest extends UserTest {

    @Test
    public void findExistingUserById() {
        var expectedUser = registerValidUser();
        var foundUser = userController.findById(expectedUser.getId());
        assert expectedUser.getLogin().equals(foundUser.getLogin());
        assert expectedUser.getName().equals(foundUser.getName());
    }

    @Test
    public void findExistingUserByLogin() {
        var expectedUser = registerValidUser();
        var foundUser = userController.findByLogin(expectedUser.getLogin());
        assert expectedUser.getLogin().equals(foundUser.getLogin());
        assert expectedUser.getName().equals(foundUser.getName());
    }

    @Test
    public void ensureBadRequestForInvalidJwtToken() {
        Assertions.assertThrows(EventorException.class, () -> userController.enterByJwt("invalid jwt"));
    }

    @Test
    public void findAllByLoginPrefix() {
        var login = validUserLogin();
        var firstUser = userController.register(validUserRegisterDto(
                login+'a',
                validUserName(),
                validUserPassword()
        ));
        var secondUser = userController.register(validUserRegisterDto(
                login+'b',
                validUserName(),
                validUserPassword()
        ));
        var thirdUser = userController.register(validUserRegisterDto());

        var firstList = userController.findAllByLoginPrefix("");
        var secondList = userController.findAllByLoginPrefix(login);
        var thirdList = userController.findAllByLoginPrefix(login + "a");

        assert firstList.contains(firstUser);
        assert firstList.contains(secondUser);
        assert firstList.contains(thirdUser);
        assert secondList.contains(firstUser);
        assert secondList.contains(secondUser);
        assert !secondList.contains(thirdUser);
        assert thirdList.contains(firstUser);
        assert !thirdList.contains(secondUser);
        assert !thirdList.contains(thirdUser);
    }

    private UserResponseDto registerValidUser() {
        return userController.register(validUserRegisterDto());
    }
}
