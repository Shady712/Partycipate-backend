package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class InviteFindTest extends InviteTest {

    @Test
    public void findExistingInviteById() {
        var invite = createInvite();
        assert invite.equals(inviteController.findById(invite.getId()));
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        createInvite();
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findById(0L)
        );
    }

    @Test
    public void findAllIncoming() {
        var userRegisterDto = validUserRegisterDto();
        assert Stream.of(
                createValidEvent(),
                createValidEvent(),
                createValidEvent()
        )
                .map(eventResponseDto -> validInviteCreateDto(eventResponseDto, registerUser(userRegisterDto)))
                .map(this::createInvite)
                .toList()
                .equals(inviteController.findAllIncoming(getJwt(userRegisterDto)));
        assert !inviteController.findAllIncoming(getJwt(userRegisterDto)).contains(createInvite());
    }

    @Test
    public void ensureBadRequestForInvalidReceiverJwt() {
        createInvite();
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findAllIncoming("invalid jwt")
        );
    }

    @Test
    public void findAllByEventId() {
        var userRegisterDto = validUserRegisterDto();
        var event = createValidEvent(userRegisterDto);
        assert Stream.of(
                registerUser(),
                registerUser(),
                registerUser()
        )
                .map(receiver -> createInvite(validInviteCreateDto(event, receiver)))
                .toList()
                .equals(inviteController.findAllByEventId(event.getId(), getJwt(userRegisterDto)));
        assert !inviteController.findAllByEventId(event.getId(), getJwt(userRegisterDto)).contains(createInvite());
    }

    @Test
    public void ensureBadRequestForInvalidCreatorJwt() {
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findAllByEventId(createInvite().getEvent().getId(), "invalid jwt")
        );
    }

    @Test
    public void ensureBadRequestForInvalidEventId() {
        var userRegisterDto = validUserRegisterDto();
        createInvite(validInviteCreateDto(createValidEvent(), registerUser(userRegisterDto)));
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findAllByEventId(0L, getJwt(userRegisterDto))
        );
    }

    @Test
    public void ensureBadRequestForInvalidCreator() {
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findAllByEventId(createInvite().getEvent().getId(), validJwt())
        );
    }
}
