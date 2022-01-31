package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.dtos.InviteResponseDto;
import com.sasd.eventor.model.enums.RequestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;
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

    @Test
    public void ensureBadRequestForInvalidId() {
        var receiver = validUserRegisterDto();
        inviteController.createInvite(
                validInviteCreateDto(createValidEvent(), registerUser(receiver))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.acceptInvite(Long.MAX_VALUE, getJwt(receiver))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(Long.MAX_VALUE, getJwt(receiver))
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        var inviteId = inviteController.createInvite(
                        validInviteCreateDto(createValidEvent(), registerUser(validUserRegisterDto())))
                .getId();
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.acceptInvite(inviteId, "InvalidJwt")
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(inviteId, "InvalidJwt")
        );
    }

    @Test
    public void ensureBadRequestForInvalidReceiver() {
        var inviteId = inviteController.createInvite(
                        validInviteCreateDto(createValidEvent(), registerUser(validUserRegisterDto())))
                .getId();
        var invalidJwt = getJwt(validUserRegisterDto());
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.acceptInvite(inviteId, invalidJwt)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(inviteId, invalidJwt)
        );
    }

    @Test
    public void ensureBadRequestForInvalidStatus() {
        var receiver = validUserRegisterDto();
        var inviteId = inviteController.createInvite(
                        validInviteCreateDto(createValidEvent(), registerUser(receiver)))
                .getId();
        var rejectedInviteId = inviteController.createInvite(
                        validInviteCreateDto(createValidEvent(), registerUser(receiver)))
                .getId();
        var receiverJwt = getJwt(receiver);

        inviteController.rejectInvite(rejectedInviteId, receiverJwt);
        inviteController.acceptInvite(inviteId, receiverJwt);

        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.acceptInvite(inviteId, receiverJwt)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(inviteId, receiverJwt)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(rejectedInviteId, receiverJwt)
        );
    }

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
        var receiver = validUserRegisterDto();
        var invite = inviteController.createInvite(
                validInviteCreateDto(createValidEvent(), registerUser(receiver))
        );
        var updatedInvite = inviteHandler.handleInvite(invite.getId(), getJwt(receiver));

        assert updatedInvite.getId().equals(invite.getId());
        assert updatedInvite.getEvent().equals(invite.getEvent());
        assert updatedInvite.getReceiver().equals(invite.getReceiver());
        assert updatedInvite.getStatus().equals(status);

        return updatedInvite;
    }

    private interface InviteHandler {
        InviteResponseDto handleInvite(Long id, String receiverJwt);
    }
}
