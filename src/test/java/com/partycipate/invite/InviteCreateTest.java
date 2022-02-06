package com.partycipate.invite;

import com.partycipate.exception.PartycipateException;
import com.partycipate.model.dtos.InviteCreateDto;
import com.partycipate.model.enums.RequestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InviteCreateTest extends InviteTest {

    @Test
    public void createValidInvite() {
        var dto = validInviteCreateDto();
        var invite = inviteController.createInvite(dto);

        assert invite.getEvent().getId().equals(dto.getEventId());
        assert invite.getStatus().equals(RequestStatus.WAITING);
        assert invite.getMessage().equals(MESSAGE);
        assert invite.getReceiver().getId().equals(dto.getReceiverId());
    }

    @Test
    public void ensureBadRequestForInvalidCreatorJwt() {
        var dto = validInviteCreateDto();
        dto.setCreatorJwt("invalidJwt");
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.createInvite(dto)
        );
    }

    @Test
    public void ensureBadRequestForRecreatingExistingInvite() {
        var dto = validInviteCreateDto();
        inviteController.createInvite(dto);
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.createInvite(dto)
        );
    }

    @Test
    public void ensureBadRequestForInvalidEventId() {
        var dto = validInviteCreateDto();
        dto.setEventId(Long.MAX_VALUE);
        assertThrowOnCreation(dto);
    }

    @Test
    public void ensureBadRequestForInvalidReceiverId() {
        var dto = validInviteCreateDto();
        dto.setReceiverId(Long.MAX_VALUE);
        assertThrowOnCreation(dto);
    }

    private void assertThrowOnCreation(InviteCreateDto dto) {
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.createInvite(dto)
        );
    }
}
