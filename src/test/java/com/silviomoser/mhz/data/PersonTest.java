package com.silviomoser.mhz.data;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PersonTest {

    private Validator validator;

    @BeforeTest
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testRequiredAttributes() {
        Person person = new Person();

        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertThat(violations).hasSize(5);
    }

    @DataProvider(name = "testValidFirstNameDp")
    private Object[][] testValidFirstName() {
        return new Object[][] {
                {"Silvio", true},
                {"silvio", true},
                {"环保部", true},
                {"Hans-Rudolf", true},
                {"Arkadius Vincenz", true},
                {" Silvio", false},
                {"Silvio ", false},
                {"SELECT * FROM", false},
                {"<script>", false}
        };
    }

    @Test(dataProvider = "testValidFirstNameDp")
    public void testValidFirstName(String firstName, boolean isValid) {
        Person person = new Person();
        person.setFirstName(firstName);
        Set<ConstraintViolation<Person>> violations = validator.validateProperty(person, "firstName");
        if (isValid) {
            assertThat(violations).isEmpty();
        } else {
            assertThat(violations).isNotEmpty();
        }
    }

    @DataProvider(name = "testValidLastNameDp")
    private Object[][] testValidLastNameDp() {
        return new Object[][] {
                {"Moser", true},
                {"von Moser", true},
                {"Häberli", true},
                {"Moser-Katalevskaya", true},
                {"环保部", true},
                {" Moser", false},
                {"SELECT * FROM", false},
                {"<script>", false}
        };
    }

    @Test(dataProvider = "testValidLastNameDp")
    public void testValidLastName(String lastName, boolean isValid) {
        Person person = new Person();
        person.setLastName(lastName);
        Set<ConstraintViolation<Person>> violations = validator.validateProperty(person, "lastName");
        if (isValid) {
            assertThat(violations).isEmpty();
        } else {
            assertThat(violations).isNotEmpty();
        }
    }

}