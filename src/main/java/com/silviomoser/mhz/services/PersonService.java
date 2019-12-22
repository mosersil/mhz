package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.specifications.PersonSpecifications;
import com.silviomoser.mhz.repository.PersonRepository;
import com.silviomoser.mhz.utils.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class PersonService extends AbstractCrudService<Person> {

    @Autowired
    private PersonRepository personRepository;


    public Person findByEmail(String email) {
        Optional<Person> personOptional = personRepository.findOne(PersonSpecifications.filterByEmail(email));
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


    public Collection<Person> findByNameOrCompany(String value) {
        return personRepository.findAll(PersonSpecifications.filterByNameOrCompany(value));
    }

    public void delete(Person person) throws ServiceException {
        if (person.getMemberships() == null || person.getMemberships().size() == 0) {
            personRepository.delete(person);
            log.info("Deleted person {}", FormatUtils.toFirstLastName(person));
        } else {
            throw new ServiceException("exception_notdeleteable_activemember");
        }
    }
}
