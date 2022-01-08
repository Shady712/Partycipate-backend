package com.sasd.eventor.controllers;

import com.sasd.eventor.model.dtos.Credentials;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    // TODO: receive dto instead of strings
    @PostMapping("/register")
    public void register(Credentials credentials) {
        userService.register(credentials.getLogin(), credentials.getName(), credentials.getPassword());
    }

    @GetMapping("/getById")
    public User getById(@RequestParam Long id) {
        return userService.getById(id);
    }
}
