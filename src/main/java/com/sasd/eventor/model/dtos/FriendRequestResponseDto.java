package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FriendRequestResponseDto {
    private Long id;
    private String senderLogin;
    private String receiverLogin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequestResponseDto that = (FriendRequestResponseDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(senderLogin, that.senderLogin)
                && Objects.equals(receiverLogin, that.receiverLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderLogin, receiverLogin);
    }
}
