package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.repository.PersonRepository;
import com.silviomoser.demo.utils.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;


    public Person findByEmail(String email) {
        Optional<Person> personOptional = personRepository.findByEmail(email);
        return personOptional.isPresent() ? personOptional.get() : null;
    }

    public Person add(Person person) throws ServiceException {

        if (person.getEmail()!=null && findByEmail(person.getEmail()) != null) {
            throw new ServiceException("exception_duplicateemail");
        }

        personRepository.save(person);
        log.info("Added/updated person {}", FormatUtils.toFirstLastName(person));
        return person;
    }

    public Person update(Person person) throws ServiceException {

        try {
            personRepository.save(person);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        log.info("Added/updated person {}", FormatUtils.toFirstLastName(person));
        return person;
    }

    public Collection<Person> findByLastNameContains(String value) {
        return personRepository.findByLastNameContains(value);
    }

    public void delete(Person person) throws ServiceException {
        if (person.getMemberships() == null || person.getMemberships().size() == 0) {
            personRepository.delete(person);
            log.info("Deleted person {}", FormatUtils.toFirstLastName(person));
        } else {
            throw new ServiceException("exception_notdeleteable_activemember");
        }
    }

    public Collection<Person> findAll() {
        return personRepository.findAll();
    }
}
