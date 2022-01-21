package com.sasd.eventor.invite;

import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.controllers.InviteController;
import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.daos.InviteRepository;
import static com.sasd.eventor.utils.EventUtils.validEventCreateDtoWithoutJwt;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;
import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.services.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class InviteTest {
    @Autowired
    protected InviteController inviteController;
    @Autowired
    protected InviteRepository inviteRepository;
    @Autowired
    protected UserController userController;
    @Autowired
    protected EventController eventController;
    @Autowired
    protected JwtService jwtService;

    public String registerValidUserAndGetJwt() {
        var dto = validUserRegisterDto();
        userController.register(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }

    public Event createEvent(String jwt) {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(jwt);
        return eventController.create(eventCreateDto);
    }
}
