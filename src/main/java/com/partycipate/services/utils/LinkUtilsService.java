package com.partycipate.services.utils;

import org.springframework.stereotype.Service;

@Service
public class LinkUtilsService {
    public static final String EMAIL_VERIFICATION_LINK = "http://localhost:8080/api/v1/user/verifyEmail";
    public static final String REQUEST_PASSWORD_CHANGE_LINK = "http://localhost:8080/updatePassword";

    public static String createLinkWithLoginAndPasswordHashAsParams(String link, String login, String passwordHash) {
        return link + substituteLoginAndPasswordHashAsRequestParams(login, passwordHash);
    }

    private static String substituteLoginAndPasswordHashAsRequestParams(String login, String passwordHash) {
        return "?login=" + login + "&passwordHash=" + passwordHash;
    }
}
