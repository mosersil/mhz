package com.silviomoser.mhz.services;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
@SpringBootTest
@Transactional
@ContextConfiguration(locations = {"classpath:spring-test-config.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class})
public class PersonServiceTest {

    @Autowired
    private PersonService personService;


    @Test
    @DatabaseSetup("/services/personservice_initial.xml")
    public void testFindByEmail() {
        assertThat(personService.findByEmail("silvio@test.com")).isNull();
    }

}