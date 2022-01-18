package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.InviteCreateDto;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InviteCreateDtoToInviteEntityConverter implements Converter<InviteCreateDto, Invite> {
    private final UserService userService;
    private final EventService eventService;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Invite convert(InviteCreateDto source) {
        Invite record = new Invite();
        record.setEvent(eventService.findById(source.getEventId()).get());
        record.setReceiver(userService.findById(source.getReceiverId()).get());
        record.setStatus(Invite.InviteStatus.WAITING);
        record.setMessage(source.getMessage());
        return record;
    }
}
