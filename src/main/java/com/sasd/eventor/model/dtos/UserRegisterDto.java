package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class UserRegisterDto {
    @NotNull
    @NotEmpty
    @Size(min = 4, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9]*")
    private String login;
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 32)
    @Pattern(regexp = "[a-zA-Z ]*")
    private String name;
    @Email
    @NotNull
    private String email;
    @NotNull
    @Size(min = 8, max = 32)
    private String password;
}
