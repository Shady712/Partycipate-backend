package com.partycipate.invite;

import com.partycipate.controllers.EventController;
import com.partycipate.controllers.InviteController;
import com.partycipate.model.dtos.*;
import com.partycipate.utils.EventUtils;
import com.partycipate.AbstractTest;
import org.springframework.beans.factory.annotation.Autowired;

import static com.partycipate.utils.UserUtils.validUserRegisterDto;

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
        var eventCreateDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(getJwt(userRegisterDto));
        return eventController.create(eventCreateDto);
    }

    protected InviteCreateDto validInviteCreateDto() {
        var eventCreateDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventCreateDto.setJwt(getJwt());
        return validInviteCreateDto(eventController.create(eventCreateDto), eventCreateDto.getJwt());
    }

    protected InviteCreateDto validInviteCreateDto(EventResponseDto eventResponseDto, String creatorJwt) {
        return validInviteCreateDto(eventResponseDto, registerUser(), creatorJwt);
    }

    protected InviteCreateDto validInviteCreateDto(
            EventResponseDto eventResponseDto,
            UserResponseDto userResponseDto,
            String creatorJwt) {
        var dto = new InviteCreateDto();
        dto.setEventId(eventResponseDto.getId());
        dto.setReceiverId(userResponseDto.getId());
        dto.setMessage(MESSAGE);
        dto.setCreatorJwt(creatorJwt);
        return dto;
    }

    protected InviteResponseDto createInvite() {
        return createInvite(validInviteCreateDto());
    }

    protected InviteResponseDto createInvite(InviteCreateDto dto) {
        return inviteController.createInvite(dto);
    }
}
