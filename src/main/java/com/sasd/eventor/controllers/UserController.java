package com.sasd.eventor.controllers;

import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/user/v1")
public class UserController {
    private final UserService userService;

    // TODO: receive dto instead of strings
    @PostMapping("/register")
    public void register(String login, String name, String password) {
        userService.register(login, name, password);
    }
}
