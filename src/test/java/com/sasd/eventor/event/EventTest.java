package com.sasd.eventor.event;

import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.daos.EventRepository;
import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public abstract class EventTest {
    @Autowired
    protected EventController eventController;
    @Autowired
    protected EventRepository eventRepository;
    @Autowired
    protected UserController userController;

    protected static final String VALID_NAME = "Existential flex";
    protected static final LocalDateTime VALID_DATE = LocalDateTime.parse("2022-01-15T10:15:30");
    protected static final String VALID_DESCRIPTION = "test description";
    protected static final String VALID_LOCATION = "Saint-Petersburg, hookah way club";
    protected static final Integer VALID_PRICE = 2000;

    protected static final String VALID_USER_LOGIN = "Valid12345";
    protected static final String VALID_USER_NAME = "Valid Name";
    protected static final String VALID_USER_PASSWORD = "QWErty12345";

    protected static EventCreateDto validEventCreateDtoWithoutJwt() {
        var dto = new EventCreateDto();
       dto.setName(VALID_NAME);
        dto.setDate(VALID_DATE);
        dto.setDescription(VALID_DESCRIPTION);
        dto.setLocation(VALID_LOCATION);
        dto.setPrice(VALID_PRICE);
        return dto;
    }

    protected static EventCreateDto validEventCreateDtoWithoutJwtSnd() {
        var dto = new EventCreateDto();
        dto.setName(VALID_NAME+'1');
        dto.setDate(VALID_DATE);
        dto.setDescription(VALID_DESCRIPTION+'1');
        dto.setLocation(VALID_LOCATION+'1');
        dto.setPrice(VALID_PRICE+1);
        return dto;
    }

    protected String validJwt() {
        var dto = new UserRegisterDto();
        dto.setLogin(VALID_USER_LOGIN);
        dto.setName(VALID_USER_NAME);
        dto.setPassword(VALID_USER_PASSWORD);
        userController.register(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    protected String validJwtSnd() {
        var dto = new UserRegisterDto();
        dto.setLogin(VALID_USER_LOGIN+"Snd");
        dto.setName(VALID_USER_NAME+"Snd");
        dto.setPassword(VALID_USER_PASSWORD+"Snd");
        userController.register(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    protected void clearDb() {
        eventRepository.deleteAll();
    }
}
