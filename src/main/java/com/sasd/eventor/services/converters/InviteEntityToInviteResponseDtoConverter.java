package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.InviteResponseDto;
import com.sasd.eventor.model.entities.Invite;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InviteEntityToInviteResponseDtoConverter implements Converter<Invite, InviteResponseDto> {

    @Override
    public InviteResponseDto convert(Invite source) {
        var record = new InviteResponseDto();
        record.setId(source.getId());
        record.setReceiverLogin(source.getReceiver().getLogin());
        record.setEventId(source.getEvent().getId());
        record.setMessage(source.getMessage());
        record.setStatus(source.getStatus());
        return record;
    }
}
