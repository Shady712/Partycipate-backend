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
    public void ensureBadInviteForInvalidId() {
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
    public void ensureBadInviteForInvalidJwt() {
        var invite = inviteController.createInvite(
                validInviteCreateDto(createValidEvent(), registerUser(validUserRegisterDto()))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.acceptInvite(invite.getId(), "InvalidJwt")
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(invite.getId(), "InvalidJwt")
        );
    }

    @Test
    public void ensureBadInviteForInvalidReceiver() {
        var invite = inviteController.createInvite(
                validInviteCreateDto(createValidEvent(), registerUser(validUserRegisterDto()))
        );
        var invalidReceiver = validUserRegisterDto();
        registerUser(invalidReceiver);
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.acceptInvite(invite.getId(), getJwt(invalidReceiver))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(invite.getId(), getJwt(invalidReceiver))
        );
    }

    @Test
    public void ensureBadRequestForInvalidStatus() {
        var receiver = validUserRegisterDto();
        var invite = inviteController.createInvite(
                validInviteCreateDto(createValidEvent(), registerUser(receiver))
        );
        var rejectedInvite = inviteController.createInvite(
                validInviteCreateDto(createValidEvent(), registerUser(receiver))
        );
        var receiverJwt = getJwt(receiver);

        inviteController.rejectInvite(rejectedInvite.getId(), receiverJwt);
        inviteController.acceptInvite(invite.getId(), receiverJwt);

        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.acceptInvite(invite.getId(), receiverJwt)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(invite.getId(), receiverJwt)
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.rejectInvite(rejectedInvite.getId(), receiverJwt)
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
