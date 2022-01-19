package com.sasd.eventor.user;

import com.sasd.eventor.controllers.UserController;
import com.sasd.eventor.model.daos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class UserTest {
    @Autowired
    protected UserController userController;
    @Autowired
    protected UserRepository userRepository;
}
