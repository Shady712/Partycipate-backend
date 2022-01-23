package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String login;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDto that = (UserResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(login, that.login) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, name);
    }
}
