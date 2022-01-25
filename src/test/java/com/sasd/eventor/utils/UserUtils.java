package com.sasd.eventor.utils;

import com.sasd.eventor.model.dtos.UserRegisterDto;

import static org.apache.commons.lang3.RandomStringUtils.*;

public class UserUtils {

    public static String validUserLogin() {
        return randomAlphanumeric(4, 23);
    }

    public static String validUserName() {
        return randomAlphabetic(2, 23) + randomAlphabetic(2, 23);
    }

    public static String validUserPassword() {
        return randomAscii(8, 33);
    }

    public static UserRegisterDto validUserRegisterDto() {
        return validUserRegisterDto(validUserLogin(), validUserName(), validUserPassword());
    }

    public static UserRegisterDto validUserRegisterDto(String login, String name, String password) {
        var dto = new UserRegisterDto();
        dto.setLogin(login);
        dto.setName(name);
        dto.setPassword(password);
        return dto;
    }
}
