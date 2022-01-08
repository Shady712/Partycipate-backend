package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.UserCredentialsDto;
import com.sasd.eventor.model.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialsDtoToUserEntityConverter implements Converter<UserCredentialsDto, User> {

    @Override
    public User convert(UserCredentialsDto source) {
        var record = new User();
        record.setLogin(source.getLogin());
        record.setName(source.getName());
        record.setPassword(source.getPassword());
        return record;
    }
}
