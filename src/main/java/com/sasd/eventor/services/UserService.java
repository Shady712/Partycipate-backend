package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.dtos.UserCredentialsDto;
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

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> getByLoginAndPassword(UserCredentialsDto userCredentialsDto){
        return userRepository.findByLoginAndPassword(userCredentialsDto.getLogin(), userCredentialsDto.getPassword());
    }
}
