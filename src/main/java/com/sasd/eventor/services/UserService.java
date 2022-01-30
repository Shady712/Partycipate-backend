package com.sasd.eventor.services;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.utils.JwtService;
import com.sasd.eventor.services.utils.SaltService;
import com.sasd.eventor.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final SaltService saltService;

    public User register(User user) {
        mailService.sendEmail(
                user.getEmail(),
                "Welcome to Partycipate!",
                "You have been successfully registered! Time to PARTYcipate!"
        );
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
        return checkUniqueConstraintVacancy(login, userRepository::findByLogin);
    }

    public Boolean checkEmailVacancy(String email) {
        return checkUniqueConstraintVacancy(email, userRepository::findByEmail);
    }

    public String createJwtToken(User user) {
        return jwtService.createJwtToken(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String password) {
        return saltService.checkPassword(password, user);
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

    private Boolean checkUniqueConstraintVacancy(String constraint, UniqueConstraintFinder finder) {
        return finder.findByUniqueConstraint(constraint).isEmpty();
    }

    private interface UniqueConstraintFinder {
        Optional<User> findByUniqueConstraint(String constraint);
    }
}
