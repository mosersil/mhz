package com.silviomoser.mhz.data.specifications;

import com.silviomoser.mhz.data.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecifications {

    public static Specification<Person> filterByNameOrCompany(String searchString) {
        return
                (person, cq, cb) -> cb.or(
                        cb.like(person.get("lastName"), "%" + searchString + "%"),
                        cb.like(person.get("firstName"), "%" + searchString + "%"),
                        cb.like(person.get("company"), "%" + searchString + "%")
                );
    }


    public static Specification<Person> filterByEmail(String searchString) {
        return (person, cq, cb) -> cb.like(person.get("email"), searchString);
    }

}
