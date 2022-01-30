package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.UserResponseDto;
import com.sasd.eventor.model.entities.User;
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
        return record;
    }
}
