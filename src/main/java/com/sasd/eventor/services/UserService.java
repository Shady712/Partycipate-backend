package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.UserRepository;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.services.utils.JwtService;
import com.sasd.eventor.services.utils.SaltService;
import com.sasd.eventor.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User register(User user) {
        logger.info("Registering user '{}', name is '{}', email is '{}'",
                user.getLogin(), user.getName(), user.getEmail()
        );
        mailService.sendEmail(
                user.getEmail(),
                "Welcome to Partycipate!",
                "You have been successfully registered! Time to PARTYcipate!"
        );
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        logger.debug("Received request for finding user with id '{}'", id);
        return userRepository.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        logger.debug("Received request for finding user with login '{}'", login);
        return userRepository.findByLogin(login);
    }

    public Optional<User> findByJwt(String jwt) {
        logger.debug("Received request for finding user by jwt");
        return findById(jwtService.decodeJwtToId(jwt));
    }

    public boolean checkLoginVacancy(String login) {
        logger.debug("Received request for checking login '{}' vacancy", login);
        return checkUniqueConstraintVacancy(login, userRepository::findByLogin);
    }

    public Boolean checkEmailVacancy(String email) {
        logger.debug("Received request for checking email '{}' vacancy", email);
        return checkUniqueConstraintVacancy(email, userRepository::findByEmail);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        logger.debug("Received request for finding user by login '{}' and password '{}'", login, password);
        return userRepository.findByLoginAndPassword(login, password);
    }

    public String createJwtToken(String login, String password) {
        logger.debug("Creating jwt token for login '{}' and password '{}'", login, password);
        return jwtService.createJwtToken(findByLoginAndPassword(login, password)
                .orElseThrow(() -> new EventorException("Invalid login or password")));
    }

    public User update(User user) {
        logger.info("Updating user '{}', id is '{}', email is '{}'", user.getLogin(), user.getId(), user.getEmail());
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String password) {
        return saltService.checkPassword(password, user);
    }

    public List<User> findAllByLoginPrefix(String prefix) {
        logger.debug("Received request for finding users by login prefix '{}'", prefix);
        return userRepository.findByLoginStartingWith(prefix);
    }

    public void befriend(User first, User second) {
        logger.info("Befriending users: first login is '{}', id is '{}', second login is '{}', id is '{}'",
                first.getLogin(), first.getId(), second.getLogin(), second.getId()
        );
        first.getFriends().add(second);
        second.getFriends().add(first);
        userRepository.save(first);
        userRepository.save(second);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public List<User> findAllFriends(String login) {
        logger.debug("Received request for finding all friends for user '{}'", login);
        return userRepository.findByLogin(login).get().getFriends();
    }

    public void delete(User user) {
        logger.info("Deleting user '{}', id is '{}', email is '{}", user.getLogin(), user.getId(), user.getEmail());
        userRepository.delete(user);
    }

    private Boolean checkUniqueConstraintVacancy(String constraint, UniqueConstraintFinder finder) {
        return finder.findByUniqueConstraint(constraint).isEmpty();
    }

    private interface UniqueConstraintFinder {
        Optional<User> findByUniqueConstraint(String constraint);
    }
}
