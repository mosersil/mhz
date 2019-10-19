package com.silviomoser.mhz.api.registration;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.silviomoser.mhz.data.type.PreferredChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by silvio on 26.10.18.
 */

@Test
@SpringBootTest
@Transactional
@ContextConfiguration(locations = {"classpath:spring-test-config.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class})
public class PersonRegistrationApiTest extends AbstractTestNGSpringContextTests {


    private static final String EXPECTED_MESSAGE_VALIDATIONERROR = "Ungültige oder fehlende Eingabe";
    private static final String EXPECTED_MESSAGE_PASSWORDERROR = "Das Gewählte Password ist nicht sicher. Bitte wählen Sie ein 8-16 Zeichen langes Passwort aus Gross-/Kleinbuchstaben, Zahlen und Sonderzeichen. Keine Leerschläge.";
    private static final String STRONG_PASSWORD = "Ha123!us";
    public static final String PREFERED_CHANNEL = PreferredChannel.ELECTRONIC.name();

    @Autowired
    private PersonRegistrationApi personRegistrationApi;

    /*
    @Test
    @DatabaseSetup("/api/registration_empty.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/api/registration.xml")
    public void registerPersonTest() {
        PersonRegistrationModel personRegistrationModel = buildPersonRegistrationDataSubmission("male", "Max", "Muster", "Fake Street", "Mailbox", "5555", "Lyton", "max_muster@localhost.com",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL);
        personRegistrationApi.registerPerson(Locale.GERMAN, personRegistrationModel);

    }


    @DataProvider(name = "registerPersonValidDp")
    public Object[][] registerPersonDp() {
        return new Object[][]{
                {buildPersonRegistrationDataSubmission("male", "Max", "Muster", "Street1", "Mailbox", "8000", "Zurich", "test@test.com",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL)},
                {buildPersonRegistrationDataSubmission("female", "Max", "Muster", "Street1", "Mailbox", "8000", "Zurich", "test@test.com",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL)},
                {buildPersonRegistrationDataSubmission("female", "Max", "Muster", "Street1", null, "8000", "Zurich", "test@test.com",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL)},
                {buildPersonRegistrationDataSubmission("female", "Max", "Muster", "Street1", " ", "8000", "Zurich", "test@test.com",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL)}
        };
    }
    */

    /*
    @Test(dataProvider = "registerPersonValidDp", description = "valid transactions. We do not expect an exception here")
    public void registerPerson(PersonRegistrationModel personRegistrationDataSubmission) throws Exception {
        ResponseEntity<Long> entity = this.personRegistrationApi.registerPerson(Locale.GERMAN, personRegistrationDataSubmission);
    }
    */


    /*
    @DataProvider(name = "registerPersonInvalidDp")
    public Object[][] registerPersonInvalidDp() {
        return new Object[][]{
                {buildPersonRegistrationDataSubmission(null, null, null, null, null, null, null, null, STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL), EXPECTED_MESSAGE_VALIDATIONERROR},
                {buildPersonRegistrationDataSubmission("female", "Max", "Muster", "Street1", null, "8000", "Zurich", " ",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL), EXPECTED_MESSAGE_VALIDATIONERROR},
                {buildPersonRegistrationDataSubmission(" ", " ", " ", " ", " ", " ", " ", " ",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL), EXPECTED_MESSAGE_VALIDATIONERROR},
                {buildPersonRegistrationDataSubmission("invalid", "Max", "Muster", "Street1", " ", " ", " ", "test@test.com",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL), EXPECTED_MESSAGE_VALIDATIONERROR},
                {buildPersonRegistrationDataSubmission("female", "Max", "Muster", "Street1", null, "8000", "Zurich", "test.com",STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL), EXPECTED_MESSAGE_VALIDATIONERROR},
                {buildPersonRegistrationDataSubmission("male", "Max", "Muster", "Street1", "Mailbox", "8000", "Zurich", "test@test.com",STRONG_PASSWORD, "wrong", PREFERED_CHANNEL), EXPECTED_MESSAGE_PASSWORDERROR},
                {buildPersonRegistrationDataSubmission("male", "Max", "Muster", "Street1", "Mailbox", "8000", "Zurich", "test@test.com","", "", PREFERED_CHANNEL), EXPECTED_MESSAGE_PASSWORDERROR},
                {buildPersonRegistrationDataSubmission("male", "Max", "Muster", "Street1", "Mailbox", "8000", "Zurich", "test@test.com",null, null, PREFERED_CHANNEL), EXPECTED_MESSAGE_PASSWORDERROR}
        };
    }
    */
/*
    @Test(dataProvider = "registerPersonInvalidDp", description = "invalid transactions. We expect exceptions")
    public void registerPersonInvalid(PersonRegistrationModel personRegistrationDataSubmission, String expectedMessage) throws Exception {
        try {
            ResponseEntity<Long> entity = this.personRegistrationApi.registerPerson(Locale.GERMAN, personRegistrationDataSubmission);
            assert false;
        } catch (ApiException e) {
            assertThat(e.getMessage()).isEqualTo(expectedMessage);
        }
    }
*/

/*
    @DatabaseSetup("/api/registration_empty.xml")
    @Test(expectedExceptions = ApiException.class, expectedExceptionsMessageRegExp = "Es existiert bereits ein Account für die angegebene E-Mail Adresse.")
    public void registerSamePersonTwice() throws Exception {
        this.personRegistrationApi.registerPerson(Locale.GERMAN, buildPersonRegistrationDataSubmission("male", "Max", "Muster", "Street1", "Mailbox", "8000", "Zurich", "max_muster@localhost.com", STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL));
        this.personRegistrationApi.registerPerson(Locale.GERMAN, buildPersonRegistrationDataSubmission("male", "Max", "Muster", "Street1", "Mailbox", "8000", "Zurich", "max_muster@localhost.com", STRONG_PASSWORD, STRONG_PASSWORD, PREFERED_CHANNEL));
    }


    private PersonRegistrationModel buildPersonRegistrationDataSubmission(String gender, String firstName, String lastName,
                                                                          String address1, String address2, String zip, String city,
                                                                          String email, String password, String password_confirmation, String preferedChannel) {
        PersonRegistrationModel personRegistrationModel = new PersonRegistrationModel();
        personRegistrationModel.setGender(gender);
        personRegistrationModel.setFirstName(firstName);
        personRegistrationModel.setLastName(lastName);
        personRegistrationModel.setEmail(email);
        personRegistrationModel.setAddress1(address1);
        personRegistrationModel.setAddress2(address2);
        personRegistrationModel.setZip(zip);
        personRegistrationModel.setCity(city);
        personRegistrationModel.setPassword(password);
        personRegistrationModel.setPassword_confirmation(password_confirmation);
        personRegistrationModel.setPreferredChannel(preferedChannel);
        return personRegistrationModel;
    }
*/

    @DataProvider(name = "checkUserExistsDp")
    public Object[][] checkUserExistsDp() {
            return new Object[][] {
                    {"max_muster@localhost.com", true},
                    {"not_existing@localhost.com", false},
            };
    }

    @DatabaseSetup("/api/registration.xml")
    @Test(dataProvider = "checkUserExistsDp", enabled = false)
    public void testCheckUserExists(String username, boolean expected) {
        assertThat(personRegistrationApi.checkUserExists(username)).isEqualTo(expected);
    }
}