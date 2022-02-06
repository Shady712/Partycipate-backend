package com.partycipate.services.converters;

import com.partycipate.model.dtos.InviteCreateDto;
import com.partycipate.model.entities.Invite;
import com.partycipate.model.enums.RequestStatus;
import com.partycipate.services.EventService;
import com.partycipate.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

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
        record.setStatus(RequestStatus.WAITING);
        record.setMessage(source.getMessage());
        return record;
    }
}
