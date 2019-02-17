package com.silviomoser.demo.services;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

@SpringBootTest
@Transactional
@ContextConfiguration(locations = {"classpath:spring-test-config.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class})
public class PasswordServiceTest extends AbstractTestNGSpringContextTests  {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PersonRepository personRepository;



    @Test
    @DatabaseSetup("/services/create_account_initial.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/services/create_account_expected.xml")
    public void testCreateAccount() throws ServiceException {
        Person person = personRepository.findByEmail("max_muster@localhost.com").get();
        passwordService.createAccount(person);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Max Muster already has an account")
    @DatabaseSetup("/services/create_account_initial_existing.xml")
    public void testCreateAccount_alreadyExisting() throws ServiceException {
        Person person = personRepository.findByEmail("max_muster@localhost.com").get();
        passwordService.createAccount(person);
    }
}