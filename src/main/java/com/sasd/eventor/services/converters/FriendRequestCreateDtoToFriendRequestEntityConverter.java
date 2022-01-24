package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.FriendRequestCreateDto;
import com.sasd.eventor.model.entities.FriendRequest;
import com.sasd.eventor.services.UserService;
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
        return record;
    }
}
