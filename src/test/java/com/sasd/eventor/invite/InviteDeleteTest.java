package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.Invite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sasd.eventor.utils.EventUtils.validEventCreateDtoWithoutJwt;
import static com.sasd.eventor.utils.InviteUtils.validInviteCreateDto;

public class InviteDeleteTest extends InviteTest {

    @Test
    public void ensureBadRequestForPermissionDenied() {
        var createdInviteData = new createdInviteData();
        Assertions.assertThrows(EventorException.class,
                () -> inviteController.deleteById(createdInviteData.getCreatedInvite().getId(), validJwt()));
    }

    @Test
    public void ensureBadRequestForFindingDeletedInvite() {
        var createdInviteData = new createdInviteData();
        inviteController.deleteById(createdInviteData.getCreatedInvite().getId(), createdInviteData.getCreatorJwt());
        Assertions.assertThrows(EventorException.class,
                () -> inviteController.findById(createdInviteData.getCreatedInvite().getId()));
    }

    @Test
    public void ensureBadRequestForDeletingUncreatedInvite() {
        var createdInviteData = new createdInviteData();
        Assertions.assertThrows(EventorException.class,
                () -> inviteController.deleteById(createdInviteData.getCreatedInvite().getId() + 100,
                        createdInviteData.getCreatorJwt()));
    }

    protected class createdInviteData {
        private final Invite createdInvite;
        private final String creatorJwt;

        private createdInviteData() {
            var eventCreateDto = validEventCreateDtoWithoutJwt();
            creatorJwt = validJwt();
            eventCreateDto.setJwt(creatorJwt);
            var createdEvent = createEvent(validJwt());
            createdInvite = inviteController.create(validInviteCreateDto(
                    jwtService.decodeJwtToId(validJwt()), createdEvent.getId(), creatorJwt));
        }

        private String getCreatorJwt() {
            return creatorJwt;
        }

        public Invite getCreatedInvite() {
            return createdInvite;
        }
    }
}