package com.partycipate.invite;

import com.partycipate.exception.PartycipateException;
import com.partycipate.model.dtos.InviteResponseDto;
import com.partycipate.model.enums.RequestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.partycipate.utils.UserUtils.validUserRegisterDto;

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
        abstractHappyPathTest(((id, receiverJwt) -> inviteController.rejectInvite(id, receiverJwt)), RequestStatus.REJECTED);
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        var creator = validUserRegisterDto();
        var receiver = validUserRegisterDto();
        inviteController.createInvite(
                validInviteCreateDto(createValidEvent(creator), registerUser(receiver), getJwt(creator))
        );
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.acceptInvite(Long.MAX_VALUE, getJwt(receiver))
        );
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.rejectInvite(Long.MAX_VALUE, getJwt(receiver))
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        var creator = validUserRegisterDto();
        var inviteId = inviteController.createInvite(validInviteCreateDto(
                createValidEvent(creator),
                registerUser(validUserRegisterDto()),
                getJwt(creator)
        )).getId();
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.acceptInvite(inviteId, "InvalidJwt")
        );
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.rejectInvite(inviteId, "InvalidJwt")
        );
    }

    @Test
    public void ensureBadRequestForInvalidReceiver() {
        var creator = validUserRegisterDto();
        var inviteId = inviteController.createInvite(validInviteCreateDto(
                createValidEvent(creator),
                registerUser(validUserRegisterDto()),
                getJwt(creator)
        )).getId();
        var invalidJwt = getJwt();
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.acceptInvite(inviteId, invalidJwt)
        );
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.rejectInvite(inviteId, invalidJwt)
        );
    }

    @Test
    public void ensureBadRequestForInvalidStatus() {
        var receiver = validUserRegisterDto();
        var creator = validUserRegisterDto();
        var inviteId = inviteController.createInvite(
                        validInviteCreateDto(createValidEvent(creator), registerUser(receiver), getJwt(creator)))
                .getId();
        creator = validUserRegisterDto();
        var rejectedInviteId = inviteController.createInvite(
                        validInviteCreateDto(createValidEvent(creator), registerUser(receiver), getJwt(creator)))
                .getId();
        var receiverJwt = getJwt(receiver);

        inviteController.rejectInvite(rejectedInviteId, receiverJwt);
        inviteController.acceptInvite(inviteId, receiverJwt);

        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.acceptInvite(inviteId, receiverJwt)
        );
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.rejectInvite(inviteId, receiverJwt)
        );
        Assertions.assertThrows(
                PartycipateException.class,
                () -> inviteController.rejectInvite(rejectedInviteId, receiverJwt)
        );
    }

    private void abstractAcceptInviteTest(InviteHandler inviteHandler) {
        var invite = abstractHappyPathTest(
                inviteHandler,
                RequestStatus.ACCEPTED
        );
        assert eventController.findAllGuests(invite.getEvent().getId())
                .stream()
                .anyMatch(userResponseDto -> userResponseDto.equals(invite.getReceiver()));
    }

    private InviteResponseDto abstractHappyPathTest(InviteHandler inviteHandler, RequestStatus status) {
        var receiver = validUserRegisterDto();
        var creator = validUserRegisterDto();
        var invite = inviteController.createInvite(
                validInviteCreateDto(createValidEvent(creator), registerUser(receiver), getJwt(creator))
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
