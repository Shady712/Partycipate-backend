package com.sasd.eventor.services;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public User register(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> findByJwt(String jwt) {
        return findById(jwtService.decodeJwtToId(jwt));
    }

    public boolean checkLoginVacancy(String login) {
        return userRepository.findByLogin(login).isEmpty();
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    public String createJwtToken(String login, String password) {
        return jwtService.createJwtToken(findByLoginAndPassword(login, password)
                .orElseThrow(() -> new EventorException("Invalid login or password")));
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllByLoginPrefix(String prefix) {
        return userRepository.findByLoginStartingWith(prefix);
    }
}
