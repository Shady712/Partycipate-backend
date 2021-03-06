package com.partycipate.services.converters;

import com.partycipate.model.dtos.UserResponseDto;
import com.partycipate.model.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToUserResponseDtoConverter implements Converter<User, UserResponseDto> {

    @Override
    public UserResponseDto convert(User source) {
        var record = new UserResponseDto();
        record.setId(source.getId());
        record.setLogin(source.getLogin());
        record.setName(source.getName());
        record.setEmail(source.getEmail());
        record.setTelegramUrl(source.getTelegramUrl());
        return record;
    }
}
