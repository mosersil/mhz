package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;


@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByResetToken(String token);

    Collection<User> findByUsernameContains(String searchString);
}
