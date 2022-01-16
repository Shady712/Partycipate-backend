package com.sasd.eventor.services.converters;

import com.sasd.eventor.model.dtos.UserUpdateDto;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserUpdateDtoToUserEntityConverter implements Converter<UserUpdateDto, User> {
    private final UserService userService;

    @Override
    public User convert(UserUpdateDto source) {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        var record = userService.findByJwt(source.getJwt()).get();
        record.setLogin(source.getLogin());
        record.setName(source.getName());
        return record;
    }
}