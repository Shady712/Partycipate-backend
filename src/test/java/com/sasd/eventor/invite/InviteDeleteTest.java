package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;
import static com.sasd.eventor.utils.EventUtils.validEventCreateDtoWithoutJwt;

public class InviteDeleteTest extends InviteTest{

    @Test
    public void deleteExistingInvite() {
        var creatorDto = validUserRegisterDto();
        var creatorJwt = getJwt(creatorDto);
        registerUser(creatorDto);
        var eventDto = validEventCreateDtoWithoutJwt();
        eventDto.setJwt(creatorJwt);
        var invite = inviteController.createInvite(
                validInviteCreateDto(eventController.create(eventDto), registerUser(validUserRegisterDto())
        ));
        inviteController.deleteInvite(invite.getId(), creatorJwt);
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findById(invite.getId())
        );
    }

    @Test
    public void ensureBadRequestOnDeleteNonExistingInvite() {
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.deleteInvite(Long.MAX_VALUE, getJwt())
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByInvalidJwt() {
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.deleteInvite(
                        inviteController.createInvite(validInviteCreateDto()).getId(),
                        "InvalidJwt"
                )
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByOtherEventCreator() {
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.deleteInvite(
                        inviteController.createInvite(validInviteCreateDto()).getId(),
                        getJwt(validUserRegisterDto())
                )
        );
    }
}
