package com.partycipate.event;

import com.partycipate.utils.EventUtils;
import com.partycipate.AbstractTest;
import com.partycipate.controllers.EventController;
import com.partycipate.controllers.InviteController;
import com.partycipate.model.dtos.EventCreateDto;
import com.partycipate.model.dtos.InviteCreateDto;
import com.partycipate.model.dtos.UserRegisterDto;
import com.partycipate.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.partycipate.utils.UserUtils.validUserRegisterDto;

public abstract class EventTest extends AbstractTest {
    @Autowired
    protected EventController eventController;
    @Autowired
    protected InviteController inviteController;
    @Autowired
    protected ConversionService conversionService;

    protected User validUser() {
        UserRegisterDto userRegisterDto = validUserRegisterDto();
        var user = conversionService.convert(userRegisterDto, User.class);
        var userResponseDto = userController.register(userRegisterDto);
        Objects.requireNonNull(user).setId(userResponseDto.getId());
        return user;
    }

    protected EventCreateDto validEventCreateDto(String name) {
        return validEventCreateDto(
                name,
                EventUtils.VALID_DATE,
                EventUtils.VALID_DESCRIPTION,
                EventUtils.VALID_LOCATION,
                EventUtils.VALID_PRICE,
                EventUtils.VALID_LATITUDE,
                EventUtils.VALID_LONGITUDE
        );
    }

    @SuppressWarnings("SameParameterValue")
    protected EventCreateDto validEventCreateDto(
            String name,
            LocalDateTime date,
            String description,
            String location,
            Integer price,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        var dto = EventUtils.validEventCreateDtoWithoutJwt(
                name,
                date,
                description,
                location,
                price,
                latitude,
                longitude
        );
        dto.setJwt(getJwt());
        return dto;
    }

    protected InviteCreateDto validInviteCreateDto(Long receiverId, Long EventId, String creatorJwt){
        var dto = new InviteCreateDto();
        dto.setEventId(EventId);
        dto.setReceiverId(receiverId);
        dto.setMessage("Message");
        dto.setCreatorJwt(creatorJwt);
        return dto;
    }
}
