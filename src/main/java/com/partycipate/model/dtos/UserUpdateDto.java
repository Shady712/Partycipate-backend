package com.partycipate.model.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserUpdateDto {
    @NotNull
    @NotEmpty
    @Size(min = 4, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9]*")
    private String login;
    @NotNull
    private String jwt;
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 32)
    @Pattern(regexp = "[a-zA-Z ]*")
    private String name;
    @URL
    @Pattern(regexp = "^https://t.me/[^/]+")
    private String telegramUrl;
}
