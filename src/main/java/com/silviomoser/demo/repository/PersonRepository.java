package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by silvio on 29.08.18.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
