package com.silviomoser.demo.services;

import com.silviomoser.demo.config.PwResetConfiguration;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.repository.RoleRepository;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.security.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.silviomoser.demo.security.utils.PasswordUtils.generateToken;
import static com.silviomoser.demo.security.utils.PasswordUtils.isValidPassword;
import static com.silviomoser.demo.utils.FormatUtils.toFirstLastName;
import static com.silviomoser.demo.utils.StringUtils.isBlank;

@Service
@Slf4j
public class PasswordService {

    @Autowired
    public EmailSenderService emailSenderService;
    @Autowired
    public PwResetConfiguration pwResetConfiguration;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void startPwResetSelfService(String username, String forward) throws ServiceException {
        final Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            user.setResetToken(generateToken(50, false));
            userRepository.save(user);

            final String subject = String.format("%s hat sein Passwort vergessen?", user.getPerson().getFirstName());

            emailSenderService.sendSimpleMail(pwResetConfiguration.getEmailFrom(), user.getPerson().getEmail(), subject, renderEmailText(user, forward));

        } else {
            log.warn("User {} does not exist");
            throw new ServiceException("Invalid userId");
        }
    }

    public User redeemToken(String token, String newPassword, String newPasswordConfirmation) throws ServiceException {
        if (isBlank(token)) {
            throw new ServiceException("Token must not be blank");
        }
        final Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (!optionalUser.isPresent()) {
            log.warn("Invalid resettoken");
            throw new ServiceException("Invalid reset token");
        }


        if (!isValidPassword(newPassword, newPasswordConfirmation)) {
            throw new ServiceException("Invalid password");
        }
        final User user = optionalUser.get();
        user.setPassword(PasswordUtils.hashPassword(newPassword));
        user.setResetToken(null);
        return userRepository.save(user);
    }

    private String renderEmailText(User user, String forward) {
        return String.format("Hallo %s,\n\nDu hast dein Passwort vergessen? Nicht so schlimm, hier kannst du dir ein neues Passwort setzen: %s", toFirstLastName(user.getPerson()), String.format("%s?token=%s&forward=%s", pwResetConfiguration.getLandingPage(), user.getResetToken(), forward));
    }


    public void createAccount(Person person) throws ServiceException {
        Optional<Role> optionalRole = roleRepository.findByType(RoleType.USER);
        if (!optionalRole.isPresent()) {
            throw new ServiceException("Default role USER not available");
        }
        createAccount(person, optionalRole.get());
    }


    public void resetAccount(Person person) throws ServiceException {
        final User user = person.getUser();
        if (user!=null) {

            final String passwordClearText = PasswordUtils.generateToken(8,false);
            user.setPassword(PasswordUtils.hashPassword(passwordClearText));
            user.setLastModifiedDate(LocalDateTime.now());
            userRepository.save(user);
            final String subject = String.format("Passwort reset für %s", user.getPerson().getFirstName());

            emailSenderService.sendSimpleMail(pwResetConfiguration.getEmailFrom(), user.getPerson().getEmail(), subject, renderPwResetEmailText(user, passwordClearText));

        } else {
            log.warn(String.format("%s already has an account", toFirstLastName(person)));
            throw new ServiceException(String.format("%s already has an account", toFirstLastName(person)));
        }
    }


    public void createAccount(Person person, Role... roles) throws ServiceException {
        if (isBlank(person.getEmail())) {
            throw new ServiceException("Email Address is mandatory");
        }
        if (person.getUser()==null) {
            final User user = new User();
            user.setPerson(person);
            final String passwordClearText = PasswordUtils.generateToken(8,false);
            user.setPassword(PasswordUtils.hashPassword(passwordClearText));
            user.setCreatedDate(LocalDateTime.now());
            user.setUsername(person.getEmail());
            if (roles!=null && roles.length>0) {
                user.setRoles(Arrays.asList(roles));
            }
            userRepository.save(user);
            final String subject = String.format("Neuer Account für %s", user.getPerson().getFirstName());

            emailSenderService.sendSimpleMail(pwResetConfiguration.getEmailFrom(), user.getPerson().getEmail(), subject, renderWelcomeEmailText(user, passwordClearText));

        } else {
            log.warn(String.format("%s already has an account", toFirstLastName(person)));
            throw new ServiceException(String.format("%s already has an account", toFirstLastName(person)));
        }
    }

    private String renderWelcomeEmailText(User user, String passwordClearText) {
        return String.format("Hallo %s,\n\nDein Account auf www.mv-oberstrass.ch wurde erstellt.\n\nBenutzername: %s\nPasswort: %s\n\nZur Sicherheit empfehlen wir, das Passwort baldmöglichst duch ein selbst gewähltes zu ersetzen.\n\nVielen Dank & liebe Grüsse!", toFirstLastName(user.getPerson()), user.getUsername(), passwordClearText);
    }

    private String renderPwResetEmailText(User user, String passwordClearText) {
        return String.format("Hallo %s,\n\nDein Account auf www.mv-oberstrass.ch wurde zurückgesetzt.\n\nBenutzername: %s\nPasswort: %s\n\nZur Sicherheit empfehlen wir, das Passwort baldmöglichst duch ein selbst gewähltes zu ersetzen.\n\nVielen Dank & liebe Grüsse!", toFirstLastName(user.getPerson()), user.getUsername(), passwordClearText);
    }


}
