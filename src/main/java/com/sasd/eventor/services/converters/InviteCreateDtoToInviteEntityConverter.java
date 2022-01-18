package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.InviteCreateDto;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@AllArgsConstructor
public class InviteCreateDtoToInviteEntityConverter implements Converter<InviteCreateDto, Invite> {
    private final UserService userService;
    private final EventService eventService;

    @Override
    public Invite convert(InviteCreateDto source) {
        Invite invite = new Invite();
        invite.setEvent(eventService.findById(source.getEventId()).get());
        invite.setReceiver(userService.findById(source.getReceiverId()).get());
        invite.setStatus(Invite.InviteStatus.WAITING);
        invite.setMessage(source.getMessage());
        return invite;
    }
}
