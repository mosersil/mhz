package com.silviomoser.demo.services;

import com.silviomoser.demo.config.PwResetConfiguration;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.ResourceBundle;

import static com.silviomoser.demo.security.utils.PasswordUtils.generateToken;
import static com.silviomoser.demo.security.utils.PasswordUtils.hashPassword;
import static com.silviomoser.demo.utils.FormatUtils.welcomingInformal;
import static java.text.MessageFormat.format;
import static java.time.LocalDateTime.now;

@Service
public class AccountService {

    private static final String RESOURCE_BUNDLE_NAME = "email_resources";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);

    private static final String LABEL_WELCOME_SUBJECT = "welcome_subject";
    private static final String LABEL_WELCOME_TEXT = "welcome_text";

    @Autowired
    public EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public PwResetConfiguration pwResetConfiguration;


    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public Collection<User> findByUsernameLike(String searchString) throws ServiceException {
        return userRepository.findByUsernameContains(searchString);
    }

    public User update(User user) throws ServiceException {
        return userRepository.save(user);
    }

    public User add(User user) throws ServiceException {
        final String passwordClearText = generateToken(8, false);
        user.setPassword(hashPassword(passwordClearText));
        user.setCreatedDate(now());
        User persistedUser = userRepository.save(user);
        userRepository.save(persistedUser);
        final String subject = getMessage(LABEL_WELCOME_SUBJECT);
        final String text = getMessage(LABEL_WELCOME_TEXT, welcomingInformal(user.getPerson()), pwResetConfiguration.getBaseUrl(), user.getUsername(), passwordClearText);

        sendEmailConfirmation(user.getPerson(), subject, text);
        return userRepository.save(user);
    }

    public void delete(User user) throws ServiceException {
        userRepository.delete(user);
    }

    private String getMessage(String key, String... params) {
        return format(RESOURCE_BUNDLE.getString(key), params);
    }

    private void sendEmailConfirmation(Person person, String subject, String text) throws ServiceException {
        emailSenderService.sendSimpleMail(pwResetConfiguration.getEmailFrom(), person.getEmail(), subject, text);
    }

}