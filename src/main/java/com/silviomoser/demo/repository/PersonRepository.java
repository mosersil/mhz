package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by silvio on 29.08.18.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p where p.email = :email")
    Person findByEmail(@Param("email") String email);
}
