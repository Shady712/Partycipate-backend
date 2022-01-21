package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.InviteUtils.makeInviteCreateDto;

public class InviteDeleteTest extends InviteTest {

    @Test
    public void createValidInvite() {
        var jwt = registerValidUserAndGetJwt();
        var createdEvent = createEvent(jwt);
        var inviteCreateDto = makeInviteCreateDto(
                jwtService.decodeJwtToId(jwt), createdEvent.getId());
        var createdInvite = inviteController.create(inviteCreateDto);
        inviteController.deleteById(createdInvite.getId());
        Assertions.assertThrows(EventorException.class, () -> eventController.findById(createdInvite.getId()));
    }
}