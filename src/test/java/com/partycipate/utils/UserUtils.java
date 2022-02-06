package com.partycipate.utils;

import com.partycipate.model.dtos.UserRegisterDto;

import static org.apache.commons.lang3.RandomStringUtils.*;

public class UserUtils {

    public static String validUserLogin() {
        return randomAlphanumeric(4, 23);
    }

    public static String validUserName() {
        return randomAlphabetic(2, 23) + ' ' + randomAlphabetic(2, 23);
    }

    public static String validUserPassword() {
        return randomAscii(8, 33);
    }

    public static String validUserEmail() {
        return randomAlphabetic(2, 23) + "@gmail.com";
    }

    public static UserRegisterDto validUserRegisterDto() {
        return validUserRegisterDto(validUserLogin(), validUserName(), validUserPassword(), validUserEmail());
    }

    public static UserRegisterDto validUserRegisterDto(String login, String name, String password, String email) {
        var dto = new UserRegisterDto();
        dto.setLogin(login);
        dto.setName(name);
        dto.setPassword(password);
        dto.setEmail(email);
        return dto;
    }
}
