package com.partycipate.services.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LinkUtilsService {
    @Value("${email.verify.link}")
    public static String EMAIL_VERIFICATION_LINK;
    @Value("${password.change.link}")
    public static String REQUEST_PASSWORD_CHANGE_LINK;

    public static String createLinkWithLoginAndPasswordHashAsParams(String link, String login, String passwordHash) {
        return link + substituteLoginAndPasswordHashAsRequestParams(login, passwordHash);
    }

    private static String substituteLoginAndPasswordHashAsRequestParams(String login, String passwordHash) {
        return "?login=" + login + "&passwordHash=" + passwordHash;
    }
}
