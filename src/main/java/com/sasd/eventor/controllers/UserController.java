package com.sasd.eventor.controllers;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.dtos.UserResponseDto;
import com.sasd.eventor.model.dtos.UserUpdateDto;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.FriendRequestService;
import com.sasd.eventor.services.InviteService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final EventService eventService;
    private final InviteService inviteService;
    private final ConversionService conversionService;
    private final FriendRequestService friendRequestService;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        if (!userService.checkLoginVacancy(userRegisterDto.getLogin())) {
            throw new EventorException("Provided login is already in use");
        }
        if (!userService.checkEmailVacancy(userRegisterDto.getEmail())) {
            throw new EventorException("Provided email is already in use");
        }
        return conversionService.convert(
                userService.register(Objects.requireNonNull(conversionService.convert(userRegisterDto, User.class))),
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

    @GetMapping("/isEmailVacant")
    public Boolean isEmailVacant(@RequestParam String email) {
        return userService.checkEmailVacancy(email);
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
        var foundUser = userService.findByLogin(login)
                .orElseThrow(() -> new EventorException("Invalid login or password"));
        if (!userService.checkPassword(foundUser, password)) {
            throw new EventorException("Invalid login or password");
        }
        return userService.createJwtToken(foundUser);
    }

    @PutMapping("/update")
    public UserResponseDto update(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        if (userService.findByJwt(userUpdateDto.getJwt()).isEmpty()) {
            throw new EventorException("You are not authorized");
        }
        return conversionService.convert(
                userService.update(Objects.requireNonNull(conversionService.convert(userUpdateDto, User.class))),
                UserResponseDto.class
        );
    }

    @GetMapping("/findAllByLoginPrefix")
    public List<UserResponseDto> findAllByLoginPrefix(@RequestParam String prefix) {
        return userService.findAllByLoginPrefix(prefix)
                .stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .toList();
    }

    @Transactional
    @GetMapping("/findAllFriends")
    public List<UserResponseDto> findAllFriends(@RequestParam String login) {
        if (userService.findByLogin(login).isEmpty()) {
            throw new EventorException("User with provided login does not exist");
        }
        return userService.findAllFriends(login)
                .stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .toList();
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String jwt) {
        var foundUser = userService
                .findByJwt(jwt).orElseThrow(() -> new EventorException("You are not authorized"));
        if (!eventService.findAllByCreator(foundUser).isEmpty()) {
            throw new EventorException("You need to finish all the events first");
        }
        inviteService.findAllIncoming(foundUser)
                .forEach(inviteService::deleteInvite);
        friendRequestService.findAllOutgoing(foundUser)
                .forEach(friendRequestService::deleteRequest);
        friendRequestService.findAllIncoming(foundUser)
                .forEach(friendRequestService::deleteRequest);
        userService.delete(foundUser);
    }
}
