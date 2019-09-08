package com.silviomoser.mhz.services;

import com.silviomoser.mhz.config.PwResetConfiguration;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.User;
import com.silviomoser.mhz.data.type.RoleType;
import com.silviomoser.mhz.repository.RoleRepository;
import com.silviomoser.mhz.repository.UserRepository;
import com.silviomoser.mhz.security.utils.PasswordUtils;
import com.silviomoser.mhz.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.silviomoser.mhz.security.utils.PasswordUtils.*;
import static com.silviomoser.mhz.utils.FormatUtils.toFirstLastName;
import static com.silviomoser.mhz.utils.FormatUtils.welcomingInformal;
import static com.silviomoser.mhz.utils.StringUtils.isBlank;
import static java.text.MessageFormat.format;
import static java.time.LocalDateTime.now;

@Service
@Slf4j
public class PasswordService {

    private static final String RESOURCE_BUNDLE_NAME = "email_resources";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);

    private static final String LABEL_SELFRESET_SUBJECT = "self_reset_subject";
    private static final String LABEL_SELFRESET_TEXT = "self_reset_text";

    private static final String LABEL_FORCERESET_SUBJECT = "force_reset_subject";
    private static final String LABEL_FORCERESET_TEXT = "force_reset_text";

    private static final String LABEL_FORGOT_SUBJECT = "forgot_password_subject";
    private static final String LABEL_FORGOT_TEXT = "forgot_password_text";

    private static final String LABEL_WELCOME_SUBJECT = "welcome_subject";
    private static final String LABEL_WELCOME_TEXT = "welcome_text";

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

            final String subject = getMessage(LABEL_FORGOT_SUBJECT, null);
            final String text = getMessage(LABEL_FORGOT_TEXT, welcomingInformal(user.getPerson()), assembleResetLink(user, forward));

            sendEmailConfirmation(user.getPerson(), subject, text);

        } else {
            log.warn("User {} does not exist", username);
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
        user.setPassword(hashPassword(newPassword));
        if (user.getCreatedDate()==null) {
            user.setLastModifiedDate(now());
        }
        user.setLastModifiedDate(now());
        user.setResetToken(null);
        final User updatedUser = userRepository.save(user);

        final String subject = getMessage(LABEL_SELFRESET_SUBJECT);
        final String text = getMessage(LABEL_SELFRESET_TEXT, welcomingInformal(user.getPerson()), pwResetConfiguration.getBaseUrl());

        sendEmailConfirmation(user.getPerson(), subject, text);

        return updatedUser;
    }




    public void createAccount(Person person) throws ServiceException {
        Optional<Role> optionalRole = roleRepository.findByType(RoleType.USER);
        if (!optionalRole.isPresent()) {
            throw new ServiceException("Default role USER not available");
        }
        createAccount(person, optionalRole.get());
    }


    /**
     * Force account reset. Account must exist already
     * @param person
     * @throws ServiceException
     */
    public void resetAccount(Person person) throws ServiceException {
        final User user = person.getUser();
        if (user!=null) {

            final String passwordClearText = generateToken(8,false);
            user.setPassword(hashPassword(passwordClearText));
            user.setLastModifiedDate(now());
            userRepository.save(user);
            final String subject = getMessage(LABEL_FORCERESET_SUBJECT);
            final String text = getMessage(LABEL_FORCERESET_TEXT, welcomingInformal(user.getPerson()), pwResetConfiguration.getBaseUrl(), user.getUsername(), passwordClearText);

            sendEmailConfirmation(person, subject, text);

        } else {
            log.warn(String.format("%s does not have an account", toFirstLastName(person)));
            throw new ServiceException(String.format("Account does not exist for user %s", toFirstLastName(person)));
        }
    }


    /**
     * Create a new account. Requires the person does not have an account yet.
     * @param person
     * @param roles
     * @throws ServiceException
     */
    public void createAccount(Person person, Role... roles) throws ServiceException {
        if (isBlank(person.getEmail())) {
            throw new ServiceException("exception_email_mandatory");
        }
        if (person.getUser()==null) {
            final User user = new User();
            user.setPerson(person);
            final String passwordClearText = generateToken(8,false);
            user.setPassword(hashPassword(passwordClearText));
            user.setCreatedDate(now());
            user.setUsername(person.getEmail());
            User persistedUser = userRepository.save(user);
            if (roles!=null && roles.length>0) {
                Arrays.stream(roles).forEach(role -> persistedUser.addRole(role));
            }
            persistedUser.setActive(true);
            userRepository.save(persistedUser);
            final String subject = getMessage(LABEL_WELCOME_SUBJECT);
            final String text = getMessage(LABEL_WELCOME_TEXT, welcomingInformal(person), pwResetConfiguration.getBaseUrl(), user.getUsername(), passwordClearText);

            sendEmailConfirmation(person, subject, text);

        } else {
            log.warn(String.format("%s already has an account", toFirstLastName(person)));
            throw new ServiceException(String.format("%s already has an account", toFirstLastName(person)));
        }
    }


    /**
     * Change password
     * @param currentPassword
     * @param newPassword
     * @param confirmPassword
     * @throws ServiceException
     */
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) throws ServiceException {
        final User user = userRepository.findById(SecurityUtils.getMe().getUser().getId()).get();
        if (!PasswordUtils.matches(currentPassword, user.getPassword())) {
            throw new ChangePasswordException("currentPassword", "exception_pwchange_currentpwincorrect");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new ChangePasswordException("confirmPassword", "exception_pwchange_confirmationdoesnotmatch");
        }

        if (!isValidPassword(newPassword)) {
            throw new ChangePasswordException("newPassword", "exception_pwchange_policyviolation");
        }
        user.setPassword(hashPassword(newPassword));
        user.setLastModifiedDate(now());
        userRepository.save(user);
        userRepository.flush();
        final String subject = getMessage(LABEL_SELFRESET_SUBJECT);
        final String text = getMessage(LABEL_SELFRESET_TEXT, welcomingInformal(user.getPerson()), pwResetConfiguration.getBaseUrl());
        sendEmailConfirmation(user.getPerson(), subject, text);
    }

    private void sendEmailConfirmation(Person person, String subject, String text) throws ServiceException {
        emailSenderService.sendSimpleMail(pwResetConfiguration.getEmailFrom(), person.getEmail(), subject, text);
    }

    private String assembleResetLink(User user, String forward) throws ServiceException {
        if (user==null || isBlank(forward)) {
            throw new ServiceException("Could not assemble personalized link. Please check your configuration");
        }

        if (isBlank(user.getResetToken())) {
            throw new ServiceException("No reset token found for this user");
        }
        return String.format("%s?token=%s&forward=%s", pwResetConfiguration.getLandingPage(), user.getResetToken(), forward);
    }

    private String getMessage(String key, String... params) {
        return format(RESOURCE_BUNDLE.getString(key), params);
    }
}
