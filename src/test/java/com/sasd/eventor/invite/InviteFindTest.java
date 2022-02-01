package com.sasd.eventor.invite;

import com.sasd.eventor.exception.EventorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.sasd.eventor.utils.UserUtils.validUserRegisterDto;

public class InviteFindTest extends InviteTest {

    @Test
    public void findExistingInviteById() {
        var creator = validUserRegisterDto();
        var receiver = validUserRegisterDto();
        var invite = createInvite(validInviteCreateDto(
                createValidEvent(creator),
                registerUser(receiver),
                getJwt(creator)
        ));
        assert invite.equals(inviteController.findById(invite.getId(), getJwt(creator)));
        assert invite.equals(inviteController.findById(invite.getId(), getJwt(receiver)));
    }

    @Test
    public void ensureBadRequestForInvalidId() {
        var creator = validUserRegisterDto();
        var receiver = validUserRegisterDto();
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findById(Long.MAX_VALUE, getJwt(creator))
        );
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findById(Long.MAX_VALUE, getJwt(receiver))
        );
    }

    @Test
    public void ensureBadRequestForInvalidJwt() {
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findById(createInvite().getId(), "InvalidJwt")
        );
    }

    @Test
    public void findAllIncoming() {
        var userRegisterDto = validUserRegisterDto();
        var userResponseDto = registerUser(userRegisterDto);

        assert Stream.of(
                        validUserRegisterDto(),
                        validUserRegisterDto(),
                        validUserRegisterDto()
                )
                .map(dto -> validInviteCreateDto(createValidEvent(dto), userResponseDto, getJwt(dto)))
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
                .map(receiver -> createInvite(validInviteCreateDto(event, receiver, getJwt(userRegisterDto))))
                .toList()
                .equals(inviteController.findAllByEventId(event.getId(), getJwt(userRegisterDto)));
        assert !inviteController.findAllByEventId(event.getId(), getJwt(userRegisterDto)).contains(createInvite());
    }

    @Test
    public void ensureBadRequestForInvalidCreatorJwt() {
        var invite = createInvite();
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findAllByEventId(invite.getEvent().getId(), "invalid jwt")
        );
    }

    @Test
    public void ensureBadRequestForInvalidEventId() {
        var userRegisterDto = validUserRegisterDto();
        var creator = validUserRegisterDto();
        createInvite(validInviteCreateDto(createValidEvent(creator), registerUser(userRegisterDto), getJwt(creator)));
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findAllByEventId(Long.MAX_VALUE, getJwt(userRegisterDto))
        );
    }

    @Test
    public void ensureBadRequestForInvalidCreator() {
        var invite = createInvite();
        Assertions.assertThrows(
                EventorException.class,
                () -> inviteController.findAllByEventId(invite.getEvent().getId(), getJwt())
        );
    }
}
