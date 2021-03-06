package com.partycipate.user;

import com.partycipate.exception.PartycipateException;
import com.partycipate.model.dtos.FriendRequestCreateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.partycipate.utils.UserUtils.*;

public class UserFindTest extends UserTest {

    @Test
    public void findExistingUserById() {
        var expectedUser = registerUser();
        var foundUser = userController.findById(expectedUser.getId());
        assert expectedUser.getLogin().equals(foundUser.getLogin());
        assert expectedUser.getName().equals(foundUser.getName());
    }

    @Test
    public void findExistingUserByLogin() {
        var expectedUser = registerUser();
        var foundUser = userController.findByLogin(expectedUser.getLogin());
        assert expectedUser.getLogin().equals(foundUser.getLogin());
        assert expectedUser.getName().equals(foundUser.getName());
    }

    @Test
    public void ensureBadRequestForInvalidJwtToken() {
        Assertions.assertThrows(PartycipateException.class, () -> userController.enterByJwt("invalid jwt"));
    }

    @Test
    public void findAllByLoginPrefix() {
        var login = validUserLogin();
        var firstUser = userController.register(validUserRegisterDto(
                login + 'a',
                validUserName(),
                validUserPassword(),
                validUserEmail()
        ));
        var secondUser = userController.register(validUserRegisterDto(
                login + 'b',
                validUserName(),
                validUserPassword(),
                validUserEmail()
        ));
        var thirdUser = userController.register(validUserRegisterDto());

        var firstList = userController.findAllByLoginPrefix("");
        var secondList = userController.findAllByLoginPrefix(login);
        var thirdList = userController.findAllByLoginPrefix(login + "a");

        assert firstList.contains(firstUser);
        assert firstList.contains(secondUser);
        assert firstList.contains(thirdUser);
        assert secondList.contains(firstUser);
        assert secondList.contains(secondUser);
        assert !secondList.contains(thirdUser);
        assert thirdList.contains(firstUser);
        assert !thirdList.contains(secondUser);
        assert !thirdList.contains(thirdUser);
    }

    @Test
    public void findAllFriendsByValidLogin() {
        var sender = validUserRegisterDto();
        var firstFriend = validUserRegisterDto();
        var secondFriend = validUserRegisterDto();
        registerUser(sender);
        var friendRequestCreateDto = new FriendRequestCreateDto();
        friendRequestCreateDto.setSenderJwt(userController.createJwt(sender.getLogin(), sender.getPassword()));
        Stream.of(firstFriend, secondFriend).forEach(friend -> {
            registerUser(friend);
            friendRequestCreateDto.setReceiverLogin(friend.getLogin());
            friendRequestController.acceptRequest(
                    friendRequestController.createRequest(friendRequestCreateDto).getId(),
                    userController.createJwt(friend.getLogin(), friend.getPassword())
            );
        });

        var friends = userController.findAllFriends(sender.getLogin());

        assert Stream.of(
                        firstFriend.getLogin(),
                        secondFriend.getLogin()
                ).map(login -> userController.findByLogin(login)).toList()
                .equals(friends);
        assert !friends.contains(registerUser());
    }

    @Test
    public void ensureBadRequestOnFindAllFriendsByInvalidLogin() {
        registerUser();
        Assertions.assertThrows(
                PartycipateException.class,
                () -> userController.findAllFriends(validUserLogin())
        );
    }
}
