package com.silviomoser.demo.api.registration;

import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.builder.PersonBuilder;
import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.repository.PersonRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class PersonRegistrationApi {

    @Autowired
    PersonRepository personRepository;

    @ApiOperation(value = "Registration service for initial registration of a person")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CalendarEvent.class),
            @ApiResponse(code = 400, message = "Bad request")
    })

    @RequestMapping(value = "/public/api/registerPerson", method = RequestMethod.POST)
    public long registerPerson(@RequestBody PersonRegistrationDataSubmission registrationDataSubmission) {


        final Person person = new PersonBuilder()
                .gender(registrationDataSubmission.getGender())
                .firstName(registrationDataSubmission.getFirstName())
                .lastName(registrationDataSubmission.getLastName())
                .address1(registrationDataSubmission.getAddress())
                .address2(registrationDataSubmission.getAddress2())
                .zip(registrationDataSubmission.getZip())
                .city(registrationDataSubmission.getCity())
                .email(registrationDataSubmission.getEmail())
                .password(registrationDataSubmission.getPassword())
                .roles(RoleType.USER.name())
                .bulid();
        Optional<Person> maybeExisting = personRepository.findByEmail(person.getEmail());

        if (maybeExisting.isPresent()) {

        } else {
            personRepository.save(person);
        }

        return 0;
    }
}
