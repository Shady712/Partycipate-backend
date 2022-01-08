package com.sasd.eventor.controllers;

import com.sasd.eventor.model.dtos.UserCredentialsDto;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody UserCredentialsDto userCredentialsDto) {
        User user = new User();
        user.setLogin(userCredentialsDto.getLogin());
        user.setName(userCredentialsDto.getName());
        user.setPassword(userCredentialsDto.getPassword());
        userService.register(user);
    }

    @GetMapping("/getById")
    public User getById(@RequestParam Long id) {
        return userService.getById(id);
    }
}
