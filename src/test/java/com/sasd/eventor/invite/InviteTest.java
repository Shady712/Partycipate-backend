package com.sasd.eventor.invite;

import com.sasd.eventor.AbstractTest;
import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.controllers.InviteController;
import com.sasd.eventor.model.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sasd.eventor.utils.EventUtils.validEventCreateDtoWithoutJwt;
import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public abstract class InviteTest extends AbstractTest {
    @Autowired
    protected InviteController inviteController;
    @Autowired
    protected EventController eventController;

    protected static final String MESSAGE = "valid message";

    protected EventResponseDto createValidEvent() {
        return createValidEvent(validUserRegisterDto());
    }

    protected EventResponseDto createValidEvent(UserRegisterDto userRegisterDto) {
        var eventCreateDto = validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(getJwt(userRegisterDto));
        return eventController.create(eventCreateDto);
    }

    protected InviteCreateDto validInviteCreateDto() {
        return validInviteCreateDto(createValidEvent());
    }

    protected InviteCreateDto validInviteCreateDto(EventResponseDto eventResponseDto) {
        return validInviteCreateDto(eventResponseDto, registerUser());
    }

    protected InviteCreateDto validInviteCreateDto(EventResponseDto eventResponseDto, UserResponseDto userResponseDto) {
        var dto = new InviteCreateDto();
        dto.setEventId(eventResponseDto.getId());
        dto.setReceiverId(userResponseDto.getId());
        dto.setMessage(MESSAGE);
        return dto;
    }

    protected InviteResponseDto createInvite() {
        return createInvite(validInviteCreateDto());
    }

    protected InviteResponseDto createInvite(InviteCreateDto dto) {
        return inviteController.createInvite(dto);
    }
}
