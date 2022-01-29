package com.sasd.eventor.model.dtos;

import com.sasd.eventor.model.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class InviteResponseDto {
    private Long id;
    private String ReceiverLogin;
    private Long EventId;
    private String message;
    private RequestStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InviteResponseDto that = (InviteResponseDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(message, that.message)
                && Objects.equals(ReceiverLogin, that.ReceiverLogin)
                && Objects.equals(EventId, that.EventId)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, ReceiverLogin, EventId, status);
    }
}
