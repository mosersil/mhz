package com.silviomoser.mhz.data.validator;

import com.silviomoser.mhz.data.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.silviomoser.mhz.utils.StringUtils.isBlank;

public class PersonValidator implements
        ConstraintValidator<PersonConstraint, Person> {

    @Override
    public void initialize(PersonConstraint personConstraint) {
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext cxt) {

        if (isBlank(person.getFirstName())
                && isBlank(person.getLastName())
                && isBlank(person.getCompany())) {
            return false;
        }
        return true;
    }

}