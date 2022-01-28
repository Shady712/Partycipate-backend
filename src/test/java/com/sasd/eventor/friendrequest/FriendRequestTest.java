package com.sasd.eventor.friendrequest;

import com.sasd.eventor.AbstractTest;
import com.sasd.eventor.controllers.FriendRequestController;
import com.sasd.eventor.model.dtos.FriendRequestCreateDto;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public abstract class FriendRequestTest extends AbstractTest {
    @Autowired
    protected FriendRequestController friendRequestController;

    protected FriendRequestCreateDto validFriendRequestCreateDto() {
        return validFriendRequestCreateDto(validUserRegisterDto(), validUserRegisterDto());
    }

    protected FriendRequestCreateDto validFriendRequestCreateDto(UserRegisterDto senderDto, UserRegisterDto receiverDto) {
        var dto = new FriendRequestCreateDto();
        registerUser(senderDto);
        registerUser(receiverDto);
        dto.setSenderJwt(userController.createJwt(senderDto.getLogin(), senderDto.getPassword()));
        dto.setReceiverLogin(receiverDto.getLogin());
        return dto;
    }
}
