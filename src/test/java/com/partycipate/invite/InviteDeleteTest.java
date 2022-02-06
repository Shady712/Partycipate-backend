package com.partycipate.invite;

import com.partycipate.exception.PartycipateException;
import com.partycipate.utils.EventUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.partycipate.utils.UserUtils.validUserRegisterDto;
import static com.partycipate.utils.EventUtils.validEventCreateDtoWithoutJwt;

public class InviteDeleteTest extends InviteTest {

    @Test
    public void deleteExistingInvite() {
        var creatorJwt = getJwt(validUserRegisterDto());
        var eventDto = EventUtils.validEventCreateDtoWithoutJwt();
        eventDto.setJwt(creatorJwt);
        var inviteId = inviteController.createInvite(
                validInviteCreateDto(eventController.create(eventDto), registerUser(validUserRegisterDto()), eventDto.getJwt())
        ).getId();
        inviteController.deleteInvite(inviteId, creatorJwt);
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.findById(inviteId, creatorJwt)
        );
    }

    @Test
    public void ensureBadRequestOnDeleteNonExistingInvite() {
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.deleteInvite(Long.MAX_VALUE, getJwt())
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByInvalidJwt() {
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.deleteInvite(
                        inviteController.createInvite(validInviteCreateDto()).getId(),
                        "InvalidJwt"
                )
        );
    }

    @Test
    public void ensureBadRequestOnDeleteByOtherEventCreator() {
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.deleteInvite(
                        inviteController.createInvite(validInviteCreateDto()).getId(),
                        getJwt()
                )
        );
    }
}
