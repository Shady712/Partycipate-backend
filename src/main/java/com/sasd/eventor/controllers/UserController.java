package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final ConversionService conversionService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        if (!userService.checkLoginVacancy(userRegisterDto.getLogin())) {
            throw new EventorException("Provided login is already in use");
        }
        userService.register(conversionService.convert(userRegisterDto, User.class));
    }

    @GetMapping("/findById")
    public User findById(@RequestParam Long id) {
        return userService.findById(id).orElseThrow(() -> new EventorException("User with provided id does not exist"));
    }

    @GetMapping("/enter")
    public User enter(@RequestParam String login, @RequestParam String password) {
        return userService.findByLoginAndPassword(login, password).orElseThrow(() -> new EventorException("Invalid login or password"));
    }
}
