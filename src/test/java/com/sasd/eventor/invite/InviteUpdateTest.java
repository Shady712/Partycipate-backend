package com.sasd.eventor.invite;

import com.sasd.eventor.model.dtos.InviteResponseDto;
import com.sasd.eventor.model.enums.RequestStatus;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.model.enums.RequestStatus.ACCEPTED;
import static com.sasd.eventor.model.enums.RequestStatus.REJECTED;

public class InviteUpdateTest extends InviteTest {

    @Test
    public void acceptValidInvite() {
        abstractAcceptInviteTest((id, receiverJwt) -> inviteController.acceptInvite(id, receiverJwt));
    }

    @Test
    public void acceptRejectedInvite() {
        abstractAcceptInviteTest((id, receiverJwt) -> {
            inviteController.rejectInvite(id, receiverJwt);
            return inviteController.acceptInvite(id, receiverJwt);
        });
    }

    @Test
    public void rejectValidInvite() {
        abstractHappyPathTest(((id, receiverJwt) -> inviteController.rejectInvite(id, receiverJwt)), REJECTED);
    }

    //TODO bad request tests

    private void abstractAcceptInviteTest(InviteHandler inviteHandler) {
        var invite = abstractHappyPathTest(
                inviteHandler,
                ACCEPTED
        );
        assert eventController.findAllGuests(invite.getEvent().getId())
                .stream()
                .anyMatch(userResponseDto -> userResponseDto.equals(invite.getReceiver()));
    }

    private InviteResponseDto abstractHappyPathTest(InviteHandler inviteHandler, RequestStatus status) {
        var receiverDto = registerUser();
        var invite = inviteController.createInvite(
                validInviteCreateDto(
                        createValidEvent(),
                        receiverDto
                )
        );
        var updatedInvite = inviteHandler.handleInvite(invite.getId(), getJwt(receiverDto));

        assert invite.getId().equals(updatedInvite.getId());
        assert invite.getEvent().equals(updatedInvite.getEvent());
        assert invite.getReceiver().equals(updatedInvite.getReceiver());
        assert invite.getStatus().equals(status);

        return updatedInvite;
    }

    private interface InviteHandler {
        InviteResponseDto handleInvite(Long id, String receiverJwt);
    }
}
