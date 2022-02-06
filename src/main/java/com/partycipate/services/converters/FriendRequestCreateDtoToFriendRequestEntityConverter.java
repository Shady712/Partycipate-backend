package com.partycipate.services.converters;

import com.partycipate.model.dtos.FriendRequestCreateDto;
import com.partycipate.model.entities.FriendRequest;
import com.partycipate.model.enums.RequestStatus;
import com.partycipate.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FriendRequestCreateDtoToFriendRequestEntityConverter
        implements Converter<FriendRequestCreateDto, FriendRequest> {
    private final UserService userService;

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public FriendRequest convert(FriendRequestCreateDto source) {
        var record = new FriendRequest();
        record.setSender(userService.findByJwt(source.getSenderJwt()).get());
        record.setReceiver(userService.findByLogin(source.getReceiverLogin()).get());
        record.setStatus(RequestStatus.WAITING);
        return record;
    }
}
