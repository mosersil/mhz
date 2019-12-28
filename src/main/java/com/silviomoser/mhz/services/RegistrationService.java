package com.silviomoser.mhz.services;

import com.silviomoser.mhz.config.RegistrationConfiguration;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.PersonVerification;
import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.User;
import com.silviomoser.mhz.data.specifications.PersonSpecifications;
import com.silviomoser.mhz.data.type.RoleType;
import com.silviomoser.mhz.repository.PersonRepository;
import com.silviomoser.mhz.repository.PersonVerificationRepository;
import com.silviomoser.mhz.repository.RoleRepository;
import com.silviomoser.mhz.security.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.silviomoser.mhz.utils.FormatUtils.welcomingFormal;
import static com.silviomoser.mhz.utils.StringUtils.*;
import static java.lang.String.format;

@Service
@Slf4j
public class RegistrationService {

    private static final String RESOURCE_BUNDLE_NAME = "email_resources";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);

    private static final String LABEL_SIGNUP_SUBJECT = "signup_subject";
    private static final String LABEL_SIGNUP_TEXT = "signup_text";

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PersonVerificationRepository personVerificationRepository;

    @Autowired
    private RegistrationConfiguration registrationConfiguration;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MembershipService membershipService;


    public Person register(Person person, String password, String passwordConfirmation, RoleType... roles) throws ServiceException {
        if (!isValidEmailAddress(person.getEmail())) {
            log.warn("Invalid email address specified");
            throw new ServiceException("registration_exception_invalid_email");
        }

        if (isNotBlank(password)) {
            if (!PasswordUtils.isValidPassword(password, passwordConfirmation)) {
                log.warn("Password not valid");
                throw new ServiceException("registration_exception_passwordrules");
            }
        }

        final Optional<Person> maybeExisting = personRepository.findOne(PersonSpecifications.filterByEmail(person.getEmail()));

        if (maybeExisting.isPresent()) {
            log.warn("Person with email {} does already exist", maybeExisting.get().getEmail());
            throw new ServiceException("registration_exception_personalreadyexists");
        } else {
            if (isNotBlank(password)) {
                final User user = new User();
                user.setPassword(PasswordUtils.hashPassword(password));
                user.setUsername(person.getEmail());
                user.setLastModifiedDate(LocalDateTime.now());
                user.setCreatedDate(LocalDateTime.now());
                user.setPerson(person);
                user.setActive(true);
                person.setUser(user);
                if (roles != null) {
                    HashSet<Role> roleSet = new HashSet<>(roles.length);
                    Arrays.asList(roles).forEach(it -> {
                        Optional<Role> role = roleRepository.findByType(it);
                        if (role.isPresent()) {
                            roleSet.add(role.get());
                        }
                    });
                    user.setRoles(roleSet);
                }
            }

            final PersonVerification personVerification = PersonVerification.builder()
                    .person(person)
                    .challengeIssued(LocalDateTime.now())
                    .token(PasswordUtils.generateToken(40, false))
                    .build();
            person.setPersonVerification(personVerification);
            final Person savedPerson = personRepository.save(person);

            membershipService.addNewMember(person, "Passivmitglied", null, "via mhz-oberstrass.ch");


            final String subject = getMessage(LABEL_SIGNUP_SUBJECT);
            final String text = getMessage(LABEL_SIGNUP_TEXT, welcomingFormal(savedPerson), registrationConfiguration.getBaseUrl()+"/signuplanding?token=" + personVerification.getToken());

            sendEmailConfirmation(savedPerson, subject, text);

            log.debug("registered new person {}", person);
            return savedPerson;

        }
    }

    private String getMessage(String key, String... params) {
        return format(RESOURCE_BUNDLE.getString(key), params);
    }

    private void sendEmailConfirmation(Person person, String subject, String text) throws ServiceException {
        emailSenderService.sendSimpleMail(registrationConfiguration.getEmailFrom(), person.getEmail(), subject, text);
    }


    public void verifyEmailAddress(String token) throws ServiceException {
        if (isBlank(token) || token.length() > 50) {
            throw new ServiceException("registration_exception_invalidtoken");
        }
        final Optional<PersonVerification> existingPersonVerification = personVerificationRepository.findByToken(token);
        if (existingPersonVerification.isPresent()) {
            if (existingPersonVerification.get().getConfirmedDate() != null) {
                throw new ServiceException("registration_exception_alreadyconfirmed");
            }
            existingPersonVerification.get().setConfirmedDate(LocalDateTime.now());
            personVerificationRepository.save(existingPersonVerification.get());
        } else {
            throw new ServiceException("registration_exception_invalidtoken");
        }
    }


    public Person setPassword(String token, String password, String passwordConfirmation) throws ServiceException {
        final Optional<PersonVerification> existingPersonVerification = personVerificationRepository.findByToken(token);
        if (existingPersonVerification.isPresent()) {
            final Person person = existingPersonVerification.get().getPerson();
            if (existingPersonVerification.get().getConfirmedDate() == null) {
                throw new ServiceException("registration_exception_notconfirmed");
            }
            if (existingPersonVerification.get().getConfirmedDate().plus(1, ChronoUnit.HOURS).isAfter(LocalDateTime.now())) {
                if (!PasswordUtils.isValidPassword(password, passwordConfirmation)) {
                    log.warn("Password not valid");
                    throw new ServiceException("registration_exception_passwordrules");
                }

                final User user = new User();
                user.setPassword(PasswordUtils.hashPassword(password));
                user.setUsername(person.getEmail());
                user.setLastModifiedDate(LocalDateTime.now());
                user.setCreatedDate(LocalDateTime.now());
                user.setPerson(person);
                user.setActive(true);
                person.setUser(user);

                HashSet<Role> roleSet = new HashSet<>(1);

                Optional<Role> role = roleRepository.findByType(RoleType.USER);
                if (role.isPresent()) {
                    roleSet.add(role.get());
                }
                user.setRoles(roleSet);

                return personRepository.save(person);

            }
            else {
                throw new ServiceException("registration_exception_expiredtoken");
            }
        } else {
            throw new ServiceException("registration_exception_invalidtoken");
        }
    }
}
