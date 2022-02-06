package com.partycipate;

import com.partycipate.controllers.UserController;
import com.partycipate.exception.PartycipateException;
import com.partycipate.model.daos.UserRepository;
import com.partycipate.model.dtos.UserRegisterDto;
import com.partycipate.model.dtos.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.partycipate.utils.UserUtils.validUserRegisterDto;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractTest {
    @Autowired
    protected UserController userController;
    @Autowired
    private UserRepository userRepository;

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
        UserResponseDto response;
        try {
            response = userController.findByLogin(dto.getLogin());
        } catch (PartycipateException ignored) {
            response = userController.register(dto);
            mockVerifyEmail(response);
        }
        return response;
    }

    private void mockVerifyEmail(UserResponseDto dto) {
        var user = userRepository.findById(dto.getId()).orElseThrow();
        user.setEmailVerified(true);
        userRepository.save(user);
    }
}
