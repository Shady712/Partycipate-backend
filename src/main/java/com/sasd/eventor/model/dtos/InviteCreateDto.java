package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class InviteCreateDto {
    @NotNull
    private Long receiverId;
    @NotNull
    private Long eventId;
    @Size(max = 600)
    private String message;
    @NotNull
    private String jwt;
}