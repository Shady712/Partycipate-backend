package com.sasd.eventor.invite;

import com.sasd.eventor.model.entities.Invite;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.InviteUtils.makeInviteCreateDto;

public class InviteCreateTest extends InviteTest {

    @Test
    public void createValidInvite() {
        var jwt = registerValidUserAndGetJwt();
        var createdEvent = createEvent(jwt);
        var inviteCreateDto = makeInviteCreateDto(
                jwtService.decodeJwtToId(jwt), createdEvent.getId());
        var createdInvite = inviteController.create(inviteCreateDto);
        assert inviteRepository.findById(createdInvite.getId()).isPresent();
        var foundInvite = inviteRepository.findById(createdInvite.getId()).get();
        assert foundInvite.getReceiver().getLogin().equals(
                userController.findById(jwtService.decodeJwtToId(jwt)).getLogin());
        assert foundInvite.getEvent().getId().equals(createdEvent.getId());
        assert foundInvite.getMessage().equals(inviteCreateDto.getMessage());
        assert foundInvite.getStatus().equals(Invite.InviteStatus.WAITING);
    }
}
