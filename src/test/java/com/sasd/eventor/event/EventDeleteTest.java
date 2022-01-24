package com.sasd.eventor.event;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static com.sasd.eventor.utils.EventUtils.*;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class EventDeleteTest extends EventTest {

    @Test
    public void ensureBadRequestForFindingDeletedEvent() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(validJwt());
        var createdEvent = eventController.create(eventCreateDto);
        eventController.deleteById(createdEvent.getId(), getJwt());
        Assertions.assertThrows(EventorException.class, () -> eventController.findById(createdEvent.getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUncreatedEvent() {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(validJwt());
        var createdEvent = eventController.create(eventCreateDto);
        eventController.deleteById(createdEvent.getId()+100, getJwt());
        Assertions.assertThrows(EventorException.class, () -> eventController.findById(createdEvent.getId()));
    }

    private String getJwt() {
        var userRegisterDto = validUserRegisterDto();
        userController.register(userRegisterDto);
        return userController.createJwt(userRegisterDto.getLogin(), userRegisterDto.getPassword());
    }
}
