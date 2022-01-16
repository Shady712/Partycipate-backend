package com.sasd.eventor.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.dtos.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;

public class UserValidationTest extends UserTest {
    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_REGISTER_URL = "http://localhost:8080/api/v1/user/register";

    @BeforeEach
    private void init() {
        clearDb();
    }

    @Test
    public void passValidDto() {
        var template = new TestRestTemplate();
        HttpEntity<UserRegisterDto> request = new HttpEntity<>(validUserRegisterDto());
        var response = template.postForObject(USER_REGISTER_URL, request, UserResponseDto.class);
    }
}
