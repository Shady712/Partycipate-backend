package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsDto {
    private String login;
    private String name;
    private String password;
}
