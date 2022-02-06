package com.partycipate.user;

import com.partycipate.AbstractTest;
import com.partycipate.controllers.FriendRequestController;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserTest extends AbstractTest {
    @Autowired
    protected FriendRequestController friendRequestController;
}
