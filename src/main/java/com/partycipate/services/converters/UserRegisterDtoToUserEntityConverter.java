package com.partycipate.services.converters;

import com.partycipate.model.dtos.UserRegisterDto;
import com.partycipate.model.entities.User;
import com.partycipate.services.utils.SaltService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRegisterDtoToUserEntityConverter implements Converter<UserRegisterDto, User> {
    private final SaltService saltService;

    @Override
    public User convert(UserRegisterDto source) {
        var record = new User();
        record.setLogin(source.getLogin());
        record.setName(source.getName());
        record.setPasswordHash(saltService.createHash(source.getPassword()));
        record.setEmail(source.getEmail());
        record.setTelegramUrl(source.getTelegramUrl());
        return record;
    }
}
