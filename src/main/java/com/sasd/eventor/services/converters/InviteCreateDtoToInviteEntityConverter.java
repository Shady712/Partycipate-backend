package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.InviteCreateDto;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.services.EventService;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.sasd.eventor.model.enums.RequestStatus.WAITING;

@Component
@AllArgsConstructor
public class InviteCreateDtoToInviteEntityConverter implements Converter<InviteCreateDto, Invite> {
    private final UserService userService;
    private final EventService eventService;

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Invite convert(InviteCreateDto source) {
        var record = new Invite();
        record.setEvent(eventService.findById(source.getEventId()).get());
        record.setReceiver(userService.findById(source.getReceiverId()).get());
        record.setStatus(WAITING);
        record.setMessage(source.getMessage());
        return record;
    }
}
