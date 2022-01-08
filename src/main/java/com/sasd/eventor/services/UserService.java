package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void register(String login, String name, String password) {
        validateUserCredentials(login, name, password);
        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
    }

    private void validateUserCredentials(String login, String name, String password) {
        // TODO: implement validation
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
