package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String login;
    private String name;
}
