package com.sasd.eventor.user;

import com.sasd.eventor.AbstractTest;
import com.sasd.eventor.controllers.FriendRequestController;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserTest extends AbstractTest {
    @Autowired
    protected FriendRequestController friendRequestController;
}
