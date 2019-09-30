package com.silviomoser.mhz.services;

import com.silviomoser.mhz.config.PwResetConfiguration;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.User;
import com.silviomoser.mhz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.ResourceBundle;

import static com.silviomoser.mhz.security.utils.PasswordUtils.generateToken;
import static com.silviomoser.mhz.security.utils.PasswordUtils.hashPassword;
import static com.silviomoser.mhz.utils.FormatUtils.welcomingInformal;
import static java.text.MessageFormat.format;
import static java.time.LocalDateTime.now;

@Service
public class AccountService extends AbstractCrudService<User> {

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

    private String getMessage(String key, String... params) {
        return format(RESOURCE_BUNDLE.getString(key), params);
    }

    private void sendEmailConfirmation(Person person, String subject, String text) throws ServiceException {
        emailSenderService.sendSimpleMail(pwResetConfiguration.getEmailFrom(), person.getEmail(), subject, text);
    }

}
