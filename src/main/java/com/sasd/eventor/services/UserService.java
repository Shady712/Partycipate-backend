package com.sasd.eventor.services;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.utils.JwtService;
import com.sasd.eventor.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MailService mailService;

    public User register(User user) {
        mailService.sendEmail("frostmorn712@gmail.com", "test", "test message");
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

    public void befriend(User first, User second) {
        first.getFriends().add(second);
        second.getFriends().add(first);
        userRepository.save(first);
        userRepository.save(second);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public List<User> findAllFriends(String login) {
        return userRepository.findByLogin(login).get().getFriends();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
