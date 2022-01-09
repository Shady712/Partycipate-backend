package com.sasd.eventor.controllers;

import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final ConversionService conversionService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserRegisterDto userCredentialsDto) {
        userService.register(conversionService.convert(userCredentialsDto, User.class));
    }

    @GetMapping("/getById")
    public User getById(@RequestParam Long id) {
        return userService.getById(id);
    }
}
