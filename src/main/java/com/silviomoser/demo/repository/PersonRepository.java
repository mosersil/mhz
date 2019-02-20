package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by silvio on 29.08.18.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE p.email is not null ")
    List<Person> findWithEmailAddressSet();

    Optional<Person> findByEmail(String email);
}
