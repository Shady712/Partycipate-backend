package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
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
    public User register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        if (!userService.checkLoginVacancy(userRegisterDto.getLogin())) {
            throw new EventorException("Provided login is already in use");
        }
        return userService.register(conversionService.convert(userRegisterDto, User.class));
    }

    @GetMapping("/findById")
    public User findById(@RequestParam Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new EventorException("User with provided id does not exist"));
    }

    @GetMapping("/enter")
    public User enterByJwt(@RequestParam String jwt) {
        return userService.findByJwt(jwt)
                .orElseThrow(() -> new EventorException("User with provided id does not exist"));
    }

    @GetMapping("/createJwt")
    public String createJwt(@RequestParam String login, @RequestParam String password) {
        return userService.createJwtToken(login, password);
    }
}
