package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.utils.UserUtils;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InviteDeleteTest extends InviteTest {

    @Test
    public void successfulDeleting() {
        var inviteWithJwt = new InviteWithJwt();
        inviteController.deleteById(inviteWithJwt.getCreatedInvite().getId(), inviteWithJwt.getCreatorJwt());
        Assertions.assertThrows(EventorException.class,
                () -> inviteController.findById(inviteWithJwt.getCreatedInvite().getId()));
    }

    @Test
    public void ensureBadRequestForPermissionDenied() {
        var inviteWithJwt = new InviteWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> inviteController.deleteById(inviteWithJwt.getCreatedInvite().getId(), getJwt()));
    }

    @Test
    public void ensureBadRequestForDeletingUncreatedInvite() {
        var inviteWithJwt = new InviteWithJwt();
        Assertions.assertThrows(EventorException.class,
                () -> inviteController.deleteById(inviteWithJwt.getCreatedInvite().getId() + 100,
                        inviteWithJwt.getCreatorJwt()));
    }

    @Getter
    private class InviteWithJwt {
        private final Invite createdInvite;
        private final String creatorJwt;

        private InviteWithJwt() {
            var user = UserUtils.validUserRegisterDto();
            var registeredUser = userController.register(user);
            creatorJwt = userController.createJwt(user.getLogin(), user.getPassword());
            var createdEvent = createValidEvent(user);
            createdInvite = inviteController.create(validInviteCreateDto(createdEvent, registeredUser));
        }
    }
}