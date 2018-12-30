package com.silviomoser.demo.services;

import com.silviomoser.demo.config.PwResetConfiguration;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.security.utils.PasswordUtils;
import com.silviomoser.demo.utils.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Optional;

import static com.silviomoser.demo.security.utils.PasswordUtils.generateToken;
import static com.silviomoser.demo.security.utils.PasswordUtils.hashPassword;
import static com.silviomoser.demo.security.utils.PasswordUtils.isValidPassword;

@Service
@Slf4j
public class PasswordResetService {

    @Autowired
    public EmailSenderService emailSenderService;
    @Autowired
    public PwResetConfiguration pwResetConfiguration;
    @Autowired
    UserRepository userRepository;

    public void startPwReset(String username, String forward) throws ServiceException {
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
        return String.format("Hallo %s,\n\nDu hast dein Passwort vergessen? Nicht so schlimm, hier kannst du dir ein neues Passwort setzen: %s", FormatUtils.toFirstLastName(user.getPerson()), String.format("%s?token=%s&forward=%s", pwResetConfiguration.getLandingPage(), user.getResetToken(), forward));
    }


}
