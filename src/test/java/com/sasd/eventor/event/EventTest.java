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
        return validEventCreateDtoWithoutJwt(VALID_NAME, VALID_DATE, VALID_DESCRIPTION, VALID_LOCATION, VALID_PRICE);
    }

    protected static EventCreateDto validEventCreateDtoWithoutJwt(String name, LocalDateTime date, String description,
                                                                  String location, Integer price){
        var dto = new EventCreateDto();
        dto.setName(name);
        dto.setDate(date);
        dto.setDescription(description);
        dto.setLocation(location);
        dto.setPrice(price);
        return dto;
    }


    protected String validJwt() {
        return validJwt(VALID_USER_LOGIN, VALID_USER_NAME, VALID_USER_PASSWORD);
    }

    protected String validJwt(String login, String name, String password){
        var dto = new UserRegisterDto();
        dto.setLogin(login);
        dto.setName(name);
        dto.setPassword(password);
        if(userController.isLoginVacant(login)) {
            userController.register(dto);
        }
        return userController.createJwt(login, password);
    }


    protected void clearDb() {
        eventRepository.deleteAll();
    }
}
