package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.InviteUtils.validInviteCreateDto;

public class InviteDeleteTest extends InviteTest {

    @Test
    public void ensureBadRequestForPermissionDenied() {
        var jwt = validJwt();
        var createdEvent = createEvent(jwt);
        var createdInvite = inviteController.create(validInviteCreateDto(
                jwtService.decodeJwtToId(jwt), createdEvent.getId()));
        Assertions.assertThrows(EventorException.class, () -> inviteController.deleteById(createdInvite.getId(), validJwt()));
    }

    @Test
    public void ensureBadRequestForFindingDeletedInvite() {
        var jwt = validJwt();
        var createdEvent = createEvent(jwt);
        var createdInvite = inviteController.create(validInviteCreateDto(
                jwtService.decodeJwtToId(jwt), createdEvent.getId()));
        inviteController.deleteById(createdInvite.getId(), jwt);
        Assertions.assertThrows(EventorException.class, () -> inviteController.findById(createdInvite.getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUncreatedInvite() {
        var jwt = validJwt();
        var createdEvent = createEvent(jwt);
        var createdInvite = inviteController.create(validInviteCreateDto(
                jwtService.decodeJwtToId(jwt), createdEvent.getId()));
        Assertions.assertThrows(EventorException.class,
                () -> inviteController.deleteById(createdInvite.getId() + 100, jwt));
    }
}