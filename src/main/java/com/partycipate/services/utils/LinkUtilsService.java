package com.partycipate.services.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LinkUtilsService {
    @Value("${email.verify.link}")
    public String EMAIL_VERIFICATION_LINK;
    @Value("${password.change.link}")
    public String REQUEST_PASSWORD_CHANGE_LINK;

    public String createLinkWithLoginAndPasswordHashAsParams(String link, String login, String passwordHash) {
        return link + substituteLoginAndPasswordHashAsRequestParams(login, passwordHash);
    }

    private String substituteLoginAndPasswordHashAsRequestParams(String login, String passwordHash) {
        return "?login=" + login + "&passwordHash=" + passwordHash;
    }
}
