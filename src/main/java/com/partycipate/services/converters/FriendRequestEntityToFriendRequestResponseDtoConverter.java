package com.partycipate.services.converters;

import com.partycipate.model.dtos.FriendRequestResponseDto;
import com.partycipate.model.entities.FriendRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FriendRequestEntityToFriendRequestResponseDtoConverter
        implements Converter<FriendRequest, FriendRequestResponseDto> {
    private final UserEntityToUserResponseDtoConverter userEntityToUserResponseDtoConverter;

    @Override
    public FriendRequestResponseDto convert(FriendRequest source) {
        var record = new FriendRequestResponseDto();
        record.setId(source.getId());
        record.setSender(userEntityToUserResponseDtoConverter.convert(source.getSender()));
        record.setReceiver(userEntityToUserResponseDtoConverter.convert(source.getReceiver()));
        record.setStatus(source.getStatus());
        return record;
    }
}
