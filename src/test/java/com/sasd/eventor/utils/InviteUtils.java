package com.sasd.eventor.utils;

import com.sasd.eventor.model.dtos.InviteCreateDto;

public class InviteUtils {
    private static final String VALID_MESSAGE = "Not a very long message :)";

    public static InviteCreateDto validInviteCreateDto(Long receiverId, Long eventId) {
        var dto = new InviteCreateDto();
        dto.setEventId(eventId);
        dto.setReceiverId(receiverId);
        dto.setMessage(VALID_MESSAGE);
        return dto;
    }
}
