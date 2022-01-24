package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestResponseDto {
    private Long id;
    private String senderLogin;
    private String receiverLogin;
}
