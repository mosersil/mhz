package com.silviomoser.demo.api.core;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.silviomoser.demo.api.registration.PersonRegistrationApi;
import com.silviomoser.demo.api.registration.PersonRegistrationDataSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Locale;

/**
 * Created by silvio on 26.10.18.
 */

@Test
@SpringBootTest
//@ContextConfiguration(locations = { "classpath:spring-test-config.xml" })
//@TestPropertySource(locations = "classpath:default.properties")
public class ApiErrorHandlerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private PersonRegistrationApi personRegistrationApi;


    @DataProvider(name = "registerPersonDp")
    public Object[][] registerPersonDp() {
        return new Object[][] {
                {buildPersonRegistrationDataSubmission("male", "Max", "Muster", "test@test.com")}
        };
    }

    @Test(dataProvider = "registerPersonDp")
    public void registerPerson(PersonRegistrationDataSubmission personRegistrationDataSubmission) throws Exception {
        ResponseEntity<Long> entity = this.personRegistrationApi.registerPerson(Locale.GERMAN, personRegistrationDataSubmission);
        System.out.println(entity);
    }

    @Test(dataProvider = "registerPersonDp")
    public void registerPersonWrongData(PersonRegistrationDataSubmission personRegistrationDataSubmission) throws Exception {
        ResponseEntity<Long> entity = this.personRegistrationApi.registerPerson(Locale.GERMAN, personRegistrationDataSubmission);
    }


    @Test(expectedExceptions = ApiException.class)
    public void registerSamePersonTwice() throws Exception {
        ResponseEntity<Long> entity1 = this.personRegistrationApi.registerPerson(Locale.GERMAN, buildPersonRegistrationDataSubmission("male", "Max", "Muster", "maxmuster@test.com"));
        ResponseEntity<Long> entity2 = this.personRegistrationApi.registerPerson(Locale.GERMAN, buildPersonRegistrationDataSubmission("male", "Max", "Muster", "maxmuster@test.com"));
    }


    private PersonRegistrationDataSubmission buildPersonRegistrationDataSubmission(String gender, String firstName, String lastName, String email) {
        PersonRegistrationDataSubmission personRegistrationDataSubmission = new PersonRegistrationDataSubmission();
        personRegistrationDataSubmission.setGender(gender);
        personRegistrationDataSubmission.setFirstName(firstName);
        personRegistrationDataSubmission.setLastName(lastName);
        personRegistrationDataSubmission.setEmail(email);
        return personRegistrationDataSubmission;
    }

}