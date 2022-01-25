package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.dtos.UserResponseDto;
import com.sasd.eventor.model.dtos.UserUpdateDto;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final ConversionService conversionService;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        if (!userService.checkLoginVacancy(userRegisterDto.getLogin())) {
            throw new EventorException("Provided login is already in use");
        }
        return conversionService.convert(
                userService.register(conversionService.convert(userRegisterDto, User.class)),
                UserResponseDto.class
        );
    }

    @GetMapping("/findById")
    public UserResponseDto findById(@RequestParam Long id) {
        return conversionService.convert(
                userService.findById(id)
                        .orElseThrow(() -> new EventorException("User with provided id does not exist")),
                UserResponseDto.class
        );
    }

    @GetMapping("/findByLogin")
    public UserResponseDto findByLogin(@RequestParam String login) {
        return conversionService.convert(
                userService.findByLogin(login)
                        .orElseThrow(() -> new EventorException("User with provided login does not exist")),
                UserResponseDto.class
        );
    }

    @GetMapping("/isLoginVacant")
    public Boolean isLoginVacant(@RequestParam String login) {
        return userService.checkLoginVacancy(login);
    }

    @GetMapping("/enter")
    public UserResponseDto enterByJwt(@RequestParam String jwt) {
        return conversionService.convert(
                userService.findByJwt(jwt)
                        .orElseThrow(() -> new EventorException("User with provided id does not exist")),
                UserResponseDto.class
        );
    }

    @GetMapping("/createJwt")
    public String createJwt(@RequestParam String login, @RequestParam String password) {
        return userService.createJwtToken(login, password);
    }

    @PutMapping("/update")
    public UserResponseDto update(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        return conversionService.convert(
                userService.update(conversionService.convert(userUpdateDto, User.class)), UserResponseDto.class);
    }

    @GetMapping("/findAllByLoginPrefix")
    public List<UserResponseDto> findAllByLoginPrefix(@RequestParam String prefix) {
        return userService.findByLoginPrefix(prefix)
                .stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .toList();
    }
}
