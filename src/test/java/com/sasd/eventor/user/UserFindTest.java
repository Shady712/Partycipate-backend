package com.sasd.eventor.user;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserFindTest extends UserTest {

    @BeforeEach
    public void init() {
        clearDb();
    }

    @Test
    public void findExistingUserById() {
        var user = new User();
        user.setName(VALID_NAME);
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);
        var expectedUser = userRepository.save(user);
        var foundUser = userController.findById(expectedUser.getId());
        assert expectedUser.getLogin().equals(foundUser.getLogin());
        assert expectedUser.getName().equals(foundUser.getName());
    }

    @Test
    public void findExistingUserByLogin() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        var foundUser = userController.findByLogin(dto.getLogin());
        assert dto.getLogin().equals(foundUser.getLogin());
        assert dto.getName().equals(foundUser.getName());
    }

    @Test
    public void ensureBadRequestForInvalidLogin() {
        userController.register(validUserRegisterDto());
        Assertions.assertThrows(EventorException.class,
                () -> userController.createJwt(VALID_LOGIN + VALID_LOGIN, VALID_PASSWORD + VALID_PASSWORD)
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwtToken() {
        Assertions.assertThrows(EventorException.class, () -> userController.enterByJwt("invalid jwt"));
    }
}
