package com.sasd.eventor.utils;

import com.sasd.eventor.model.dtos.UserRegisterDto;

import static org.apache.commons.lang3.RandomStringUtils.*;

public class UserUtils {

    public static String validUserLogin() {
        return randomAlphanumeric(4, 33);
    }

    public static String validUserName() {
        return randomAlphabetic(2, 33) + randomAlphabetic(2, 33);
    }

    public static String validUserPassword() {
        return randomAscii(8, 33);
    }

    public static UserRegisterDto validUserRegisterDto() {
        var dto = new UserRegisterDto();
        dto.setLogin(validUserLogin());
        dto.setName(validUserName());
        dto.setPassword(validUserPassword());
        return dto;
    }
}
