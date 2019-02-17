package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;


    public Person findByEmail(String email) {
        Optional<Person> personOptional = personRepository.findByEmail(email);
        return personOptional.get();
    }
}
