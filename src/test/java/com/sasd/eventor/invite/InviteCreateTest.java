package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.InviteCreateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.model.enums.RequestStatus.WAITING;

public class InviteCreateTest extends InviteTest {

    @Test
    public void createValidInvite() {
        var dto = validInviteCreateDto();
        var invite = inviteController.createInvite(dto);

        assert invite.getEvent().getId().equals(dto.getEventId());
        assert invite.getStatus().equals(WAITING);
        assert invite.getMessage().equals(MESSAGE);
        assert invite.getReceiver().getId().equals(dto.getReceiverId());
    }

    @Test
    public void ensureBadRequestForRecreatingExistingInvite() {
        var dto = validInviteCreateDto();
        inviteController.createInvite(dto);
        Assertions.assertThrows(
                EventorException.class,
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
                EventorException.class,
                () -> inviteController.createInvite(dto)
        );
    }
}
