package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;
import static com.sasd.eventor.utils.EventUtils.validEventCreateDtoWithoutJwt;

public class InviteDeleteTest extends InviteTest {

    @Test
    public void deleteExistingInvite() {
        var creatorJwt = getJwt(validUserRegisterDto());
        var eventDto = validEventCreateDtoWithoutJwt();
        eventDto.setJwt(creatorJwt);
        var inviteId = inviteController.createInvite(
                        validInviteCreateDto(eventController.create(eventDto), registerUser(validUserRegisterDto())))
                .getId();
        inviteController.deleteInvite(inviteId, creatorJwt);
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findById(inviteId, creatorJwt)
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
                        getJwt()
                )
        );
    }
}
