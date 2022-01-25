package com.sasd.eventor.event;

import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.daos.EventRepository;
import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import com.sasd.eventor.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.sasd.eventor.utils.EventUtils.*;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

@SpringBootTest
public abstract class EventTest {
    @Autowired
    protected EventController eventController;
    @Autowired
    protected EventRepository eventRepository;
    @Autowired
    protected UserController userController;
    @Autowired
    protected ConversionService conversionService;

    protected String validJwt() {
        return validJwt(validUserRegisterDto());
    }

    protected String validJwt(UserRegisterDto dto) {
        userController.register(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    protected User validUser() {
        UserRegisterDto userRegisterDto = validUserRegisterDto();
        var user = conversionService.convert(userRegisterDto, User.class);
        var userResponseDto = userController.register(userRegisterDto);
        Objects.requireNonNull(user).setId(userResponseDto.getId());
        return user;
    }
    protected EventCreateDto validEventCreateDto(String name) {
        return validEventCreateDto(name, VALID_DATE, VALID_DESCRIPTION, VALID_LOCATION, VALID_PRICE);
    }

    protected EventCreateDto validEventCreateDto(
            String name,
            LocalDateTime date,
            String description,
            String location,
            Integer price
    ) {
        var dto = validEventCreateDtoWithoutJwt(
                name,
                date,
                description,
                location,
                price
        );
        dto.setJwt(validJwt());
        return dto;
    }
}
