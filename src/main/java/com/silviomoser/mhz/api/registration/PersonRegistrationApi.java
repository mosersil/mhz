package com.silviomoser.mhz.api.registration;

import com.captcha.botdetect.web.servlet.SimpleCaptcha;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.PersonVerification;
import com.silviomoser.mhz.data.User;
import com.silviomoser.mhz.data.type.RoleType;
import com.silviomoser.mhz.repository.PersonVerificationRepository;
import com.silviomoser.mhz.repository.UserRepository;
import com.silviomoser.mhz.services.RegistrationService;
import com.silviomoser.mhz.services.ServiceException;
import com.silviomoser.mhz.ui.i18.I18Helper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Slf4j
public class PersonRegistrationApi {

    @Autowired
    I18Helper i18Helper;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private PersonVerificationRepository personVerificationRepository;

    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "Registration service for initial registration of a person")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Long.class),
            @ApiResponse(code = 400, message = "Bad request")
    })

    @RequestMapping(value = "/api/public/registration/registerPerson", method = RequestMethod.POST)
    public Long registerPerson(HttpServletRequest request, @RequestBody PersonRegistrationModel registrationDataSubmission) {
        log.debug("called registerPerson: {}", registrationDataSubmission);

        final SimpleCaptcha captcha = SimpleCaptcha.load(request);
        if (!captcha.validate(registrationDataSubmission.getCaptchaCode(), registrationDataSubmission.getCaptchaId())) {
            throw new ApiException("Captcha check failed");
        }


        try {
            final Person person = Person.builder()
                    .gender(registrationDataSubmission.getGender())
                    .firstName(registrationDataSubmission.getFirstName())
                    .lastName(registrationDataSubmission.getLastName())
                    .address1(registrationDataSubmission.getAddress1())
                    .address2(registrationDataSubmission.getAddress2())
                    .zip(registrationDataSubmission.getZip())
                    .city(registrationDataSubmission.getCity())
                    .email(registrationDataSubmission.getEmail())
                    .preferedChannel(registrationDataSubmission.getPreferredChannel())
                    .bulid();

            Person person1 = registrationService.register(person, registrationDataSubmission.getPassword(), registrationDataSubmission.getPassword_confirmation(), RoleType.USER);
            return person1.getId();
        } catch (ServiceException e) {
            log.warn("Violated constraint {}", e.getMessage(), e);
            throw new ApiException(e.getLocalizedMessage());
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
        try {
            registrationService.verifyEmailAddress(token);
        } catch (ServiceException e) {
            throw new ApiException(e.getLocalizedMessage());
        }
        Optional<PersonVerification> existingPersonVerification = personVerificationRepository.findByToken(token);
        if (existingPersonVerification.isPresent()) {
            existingPersonVerification.get().setConfirmedDate(LocalDateTime.now());
            personVerificationRepository.save(existingPersonVerification.get());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            throw new ApiException("Invalid token submitted", HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/api/public/registration/setPassword", method = RequestMethod.POST)
    public Long setPassword(HttpServletRequest request, @Valid @RequestBody SetPasswordModel setPasswordModel) {
        log.debug("called setPassword: {}", setPasswordModel);

        try {
            Person person = registrationService.setPassword(setPasswordModel.getToken(), setPasswordModel.getPassword(), setPasswordModel.getPasswordConfirmation());
            return person.getId();

        } catch (ServiceException e) {
            throw new ApiException(e.getLocalizedMessage());
        }
    }
}
