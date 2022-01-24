package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FriendRequestCreateDto {
    @NotNull
    @NotEmpty
    private String senderJwt;
    @NotNull
    @NotBlank
    private String receiverLogin;
}
