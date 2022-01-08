package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

public class Credentials {
    @Getter @Setter private String login;
    @Getter @Setter private String name;
    @Getter @Setter private String password;
}
