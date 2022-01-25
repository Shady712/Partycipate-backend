package com.sasd.eventor.friendrequest;

import com.sasd.eventor.controllers.FriendRequestController;
import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.dtos.FriendRequestCreateDto;
import com.sasd.eventor.model.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

@SpringBootTest
public class FriendRequestTest {
    @Autowired
    protected FriendRequestController friendRequestController;
    @Autowired
    protected UserController userController;

    protected FriendRequestCreateDto validFriendRequestCreateDto() {
        return validFriendRequestCreateDto(validUserRegisterDto(), validUserRegisterDto());
    }

    protected FriendRequestCreateDto validFriendRequestCreateDto(UserRegisterDto senderDto, UserRegisterDto receiverDto) {
        var dto = new FriendRequestCreateDto();
        ensureUserRegistration(senderDto);
        ensureUserRegistration(receiverDto);
        dto.setSenderJwt(userController.createJwt(senderDto.getLogin(), senderDto.getPassword()));
        dto.setReceiverLogin(receiverDto.getLogin());
        return dto;
    }

    protected void ensureUserRegistration(UserRegisterDto dto) {
        if (userController.isLoginVacant(dto.getLogin())) {
            userController.register(dto);
        }
    }

    protected String getJwt(UserRegisterDto dto) {
        ensureUserRegistration(dto);
        return userController.createJwt(dto.getLogin(), dto.getPassword());
    }
}
