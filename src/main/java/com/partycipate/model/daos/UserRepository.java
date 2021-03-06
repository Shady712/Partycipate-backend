package com.partycipate.model.daos;

import com.partycipate.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Optional<User> findByLoginAndPasswordHash(String login, String passwordHash);
    List<User> findByLoginStartingWith(String prefix);
}
