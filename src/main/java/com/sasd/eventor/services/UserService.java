package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void register(User user) {
        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean checkLoginVacancy(String login) {
        return userRepository.findByLogin(login).isEmpty();
    }

    public Optional<User> getByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
