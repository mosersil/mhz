package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.PersonVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonVerificationRepository extends JpaRepository<PersonVerification, Long> {

    Optional<PersonVerification> findByToken(String token);
}
