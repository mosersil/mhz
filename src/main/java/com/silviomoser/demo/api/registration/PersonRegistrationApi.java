package com.silviomoser.demo.api.registration;

import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.PersonVerification;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.data.builder.PersonBuilder;
import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.repository.PersonRepository;
import com.silviomoser.demo.repository.PersonVerificationRepository;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.security.utils.PasswordUtils;
import com.silviomoser.demo.ui.i18.I18Helper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static com.silviomoser.demo.utils.StringUtils.isValidEmailAddress;

@RestController
@Slf4j
public class PersonRegistrationApi {

    @Autowired
    I18Helper i18Helper;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonVerificationRepository personVerificationRepository;

    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "Registration service for initial registration of a person")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Long.class),
            @ApiResponse(code = 400, message = "Bad request")
    })

    @RequestMapping(value = "/api/public/registration/registerPerson", method = RequestMethod.POST)
    public ResponseEntity<Long> registerPerson(Locale locale, @RequestBody PersonRegistrationDataSubmission registrationDataSubmission) {
        log.debug("called registerPerson: {}", registrationDataSubmission);
        try {
            final Person person = new PersonBuilder()
                    .gender(registrationDataSubmission.getGender())
                    .firstName(registrationDataSubmission.getFirstName())
                    .lastName(registrationDataSubmission.getLastName())
                    .address1(registrationDataSubmission.getAddress1())
                    .address2(registrationDataSubmission.getAddress2())
                    .zip(registrationDataSubmission.getZip())
                    .city(registrationDataSubmission.getCity())
                    .email(registrationDataSubmission.getEmail())
                    .password(registrationDataSubmission.getPassword())
                    .roles(RoleType.USER.name())
                    .verificationToken(PasswordUtils.generateToken(60, false))
                    .bulid();

            log.debug("created person object: {}", person);
            //self-registration requires a valid email address
            if (!isValidEmailAddress(person.getEmail())) {
                log.warn("Invalid email address specified");
                throw new ApiException(i18Helper.getMessage("registration_exception_illegalargument"));
            }

            if (!PasswordUtils.isValidPassword(registrationDataSubmission.getPassword(), registrationDataSubmission.getPassword_confirmation())) {
                log.warn("Password not valid");
                throw new ApiException(i18Helper.getMessage("registration_exception_passwordrules"));
            }

            final Optional<Person> maybeExisting = personRepository.findByEmail(person.getEmail());

            if (maybeExisting.isPresent()) {
                log.warn("Person with email {} does already exist", maybeExisting.get().getEmail());
                throw new ApiException(i18Helper.getMessage("registration_exception_personalreadyexists"));
            } else {
                final Person savedPerson = personRepository.save(person);
                log.debug("saved new person {}", person);
                return new ResponseEntity<Long>(savedPerson.getId(), HttpStatus.OK);
            }
        } catch (ConstraintViolationException cve) {
            log.warn("Violated constraint {}", cve.getMessage(), cve);
            throw new ApiException(i18Helper.getMessage("registration_exception_validationerror"));
        } catch (IllegalArgumentException iae) {
            log.warn("IllegalArgumentException caught", iae.getMessage(), iae);
            throw new ApiException(i18Helper.getMessage("registration_exception_illegalargument"));
        }
    }


    @RequestMapping(value = "/api/public/registration/checkUserExists", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkUserExists(@RequestParam String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/api/public/registration/verifyEmail", method = RequestMethod.GET)
    public ResponseEntity<Boolean> verifyEmail(@RequestParam String token) {
        Optional<PersonVerification> existingPersonVerification = personVerificationRepository.findByToken(token);
        if (existingPersonVerification.isPresent()) {
            existingPersonVerification.get().setConfirmedDate(LocalDateTime.now());
            personVerificationRepository.save(existingPersonVerification.get());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            throw new ApiException("Invalid token submitted", HttpStatus.NOT_FOUND);
        }
    }
}
