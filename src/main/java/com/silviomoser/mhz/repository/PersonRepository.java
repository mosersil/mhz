package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Person;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by silvio on 29.08.18.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE p.email is not null ")
    List<Person> findWithEmailAddressSet();

    Collection<Person> findAll(Specification<Person> specification);

    Optional<Person> findOne(Specification<Person> specification);
}
