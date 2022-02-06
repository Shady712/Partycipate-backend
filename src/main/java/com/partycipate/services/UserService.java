package com.partycipate.services;

import com.partycipate.model.daos.UserRepository;
import com.partycipate.model.entities.User;
import com.partycipate.services.utils.JwtService;
import com.partycipate.services.utils.LinkUtilsService;
import com.partycipate.services.utils.SaltService;
import com.partycipate.services.utils.MailService;
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
    private final LinkUtilsService linkUtilsService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User register(User user) {
        logger.info("Registering user '{}', name is '{}', email is '{}'",
                user.getLogin(), user.getName(), user.getEmail()
        );
        mailService.sendEmail(
                user.getEmail(),
                "Welcome to Partycipate!",
                "You have been successfully registered! Time to PARTYcipate!\n" +
                        "Please, verify your email address by following this link " +
                        linkUtilsService.createLinkWithLoginAndPasswordHashAsParams(
                                linkUtilsService.EMAIL_VERIFICATION_LINK,
                                user.getLogin(),
                                user.getPasswordHash()
                        )
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

    public String createJwtToken(User user) {
        logger.debug("Creating jwt token for user '{}'", user.getLogin());
        return jwtService.createJwtToken(user);
    }

    public boolean checkLoginVacancy(String login) {
        logger.debug("Received request for checking login '{}' vacancy", login);
        return checkUniqueConstraintVacancy(login, userRepository::findByLogin);
    }

    public Boolean checkEmailVacancy(String email) {
        logger.debug("Received request for checking email '{}' vacancy", email);
        return checkUniqueConstraintVacancy(email, userRepository::findByEmail);
    }

    public User update(User user) {
        logger.info("Updating user '{}', id is '{}', email is '{}'", user.getLogin(), user.getId(), user.getEmail());
        return userRepository.save(user);
    }

    public void verifyEmail(User user) {
        logger.info("User '{}' has verified his email address '{}'", user.getLogin(), user.getEmail());
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public Boolean checkPassword(User user, String password) {
        logger.debug("Checking password for user '{}'", user.getLogin());
        return saltService.checkPassword(password, user);
    }

    public List<User> findAllByLoginPrefix(String prefix) {
        logger.debug("Received request for finding users by login prefix '{}'", prefix);
        return userRepository.findByLoginStartingWith(prefix);
    }

    public Optional<User> findByEmail(String email) {
        logger.debug("Received request for finding user by email '{}'", email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByLoginAndPasswordHash(String login, String passwordHash) {
        logger.debug("Received request for finding user by login '{}' and password hash", login);
        return userRepository.findByLoginAndPasswordHash(login, passwordHash);
    }

    public User changePassword(User user, String newPassword) {
        logger.info("Changing user's '{}' password", user.getLogin());
        user.setPasswordHash(saltService.createHash(newPassword));
        return userRepository.save(user);
    }

    public void requestPasswordChange(User user) {
        logger.info("Sending email for password change to user '{}'", user.getLogin());
        mailService.sendEmail(
                user.getEmail(),
                "Partycipate password change",
                "If you want to change your password, follow this link: " +
                        linkUtilsService.createLinkWithLoginAndPasswordHashAsParams(
                                linkUtilsService.REQUEST_PASSWORD_CHANGE_LINK,
                                user.getLogin(),
                                user.getPasswordHash()
                        ) + "\nOtherwise, ignore this message"
        );
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
