package com.sasd.eventor.utils;

import com.sasd.eventor.model.dtos.EventCreateDto;
import com.sasd.eventor.model.dtos.EventUpdateDto;

import java.time.LocalDateTime;

public class EventUtils {
    public static final String VALID_NAME = "Existential flex";
    public static final LocalDateTime VALID_DATE = LocalDateTime.parse("2022-01-15T10:15:30");
    public static final String VALID_DESCRIPTION = "test description";
    public static final String VALID_LOCATION = "Saint-Petersburg, hookah way club";
    public static final Integer VALID_PRICE = 2000;

    public static EventCreateDto validEventCreateDtoWithoutJwt() {
        return validEventCreateDtoWithoutJwt(VALID_NAME, VALID_DATE, VALID_DESCRIPTION, VALID_LOCATION, VALID_PRICE);
    }

    public static EventCreateDto validEventCreateDtoWithoutJwt(
            String name,
            LocalDateTime date,
            String description,
            String location,
            Integer price
    ) {
        var dto = new EventCreateDto();
        dto.setName(name);
        dto.setDate(date);
        dto.setDescription(description);
        dto.setLocation(location);
        dto.setPrice(price);
        return dto;
    }

    public static EventUpdateDto validEventUpdateDto(Long id, String jwt) {
        return validEventUpdateDto(id, VALID_NAME, VALID_DATE, VALID_DESCRIPTION, VALID_LOCATION, VALID_PRICE, jwt);
    }

    public static EventUpdateDto validEventUpdateDto(
            Long id,
            String name,
            LocalDateTime date,
            String description,
            String location,
            Integer price,
            String jwt
    ) {
        var dto = new EventUpdateDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDate(date);
        dto.setDescription(description);
        dto.setLocation(location);
        dto.setPrice(price);
        dto.setJwt(jwt);
        return dto;
    }
}
