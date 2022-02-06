package com.partycipate.services.converters;

import com.partycipate.model.dtos.InviteResponseDto;
import com.partycipate.model.entities.Invite;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InviteEntityToInviteResponseDtoConverter implements Converter<Invite, InviteResponseDto> {
    private final UserEntityToUserResponseDtoConverter userEntityToUserResponseDtoConverter;
    private final EventEntityToEventResponseDtoConverter eventEntityToEventResponseDtoConverter;

    @Override
    public InviteResponseDto convert(Invite source) {
        var record = new InviteResponseDto();
        record.setId(source.getId());
        record.setReceiver(
                userEntityToUserResponseDtoConverter.convert(source.getReceiver())
        );
        record.setEvent(
                eventEntityToEventResponseDtoConverter.convert(source.getEvent())
        );
        record.setMessage(source.getMessage());
        record.setStatus(source.getStatus());
        return record;
    }
}
