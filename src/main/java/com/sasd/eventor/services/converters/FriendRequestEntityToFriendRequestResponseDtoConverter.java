package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.FriendRequestResponseDto;
import com.sasd.eventor.model.entities.FriendRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestEntityToFriendRequestResponseDtoConverter
        implements Converter<FriendRequest, FriendRequestResponseDto> {

    @Override
    public FriendRequestResponseDto convert(FriendRequest source) {
        var record = new FriendRequestResponseDto();
        record.setId(source.getId());
        record.setSenderLogin(source.getSender().getLogin());
        record.setReceiverLogin(source.getReceiver().getLogin());
        record.setStatus(source.getStatus());
        return record;
    }
}
