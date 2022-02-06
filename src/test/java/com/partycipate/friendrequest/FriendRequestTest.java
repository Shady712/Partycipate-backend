package com.partycipate.friendrequest;

import com.partycipate.AbstractTest;
import com.partycipate.controllers.FriendRequestController;
import com.partycipate.model.dtos.FriendRequestCreateDto;
import com.partycipate.model.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;

import static com.partycipate.utils.UserUtils.validUserRegisterDto;

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
