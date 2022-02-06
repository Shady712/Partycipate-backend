package com.partycipate.model.dtos;

import com.partycipate.model.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FriendRequestResponseDto {
    private Long id;
    private UserResponseDto sender;
    private UserResponseDto receiver;
    private RequestStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequestResponseDto that = (FriendRequestResponseDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(sender, that.sender)
                && Objects.equals(receiver, that.receiver)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, status);
    }
}
