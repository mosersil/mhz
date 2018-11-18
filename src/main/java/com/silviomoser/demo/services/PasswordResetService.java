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

import static com.silviomoser.demo.security.utils.PasswordUtils.hashPassword;
import static com.silviomoser.demo.security.utils.PasswordUtils.isValidPassword;

@Service
@Slf4j
public class PasswordResetService {

    @Autowired
    public JavaMailSender javaMailSender;
    @Autowired
    public PwResetConfiguration pwResetConfiguration;
    @Autowired
    UserRepository userRepository;

    public void startPwReset(String username) throws ServiceException {
        final Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            user.setResetToken(PasswordUtils.generateToken(50, false));
            userRepository.save(user);

            final MimeMessage message = javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message);

            try {
                helper.setFrom(pwResetConfiguration.getEmailFrom());
                helper.setTo(user.getPerson().getEmail());
                helper.setSubject(user.getPerson().getFirstName() + " hat das Passwort vergessen?");
                helper.setText(renderEmailText(user));
                javaMailSender.send(message);

            } catch (Exception e) {
                log.error("Exception while sending email", e);
                throw new ServiceException("Could not send email", e);
            }

        } else {
            log.warn("User {} does not exist");
            throw new ServiceException("Invalid userId");
        }
    }

    public User redeemToken(String token) throws ServiceException {
        final Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (!optionalUser.isPresent()) {
            log.warn("Invalid resettoken");
            throw new ServiceException("Invalid reset token");
        }
        return optionalUser.get();
    }

    public void updatePassword(String newPassword, String passwordConfirmation, String resetToken) throws ServiceException {
        if (!isValidPassword(newPassword, passwordConfirmation)) {
            throw new ServiceException("Bad password");
        }
        final User user = redeemToken(resetToken);
        user.setPassword(hashPassword(newPassword));
    }

    private String renderEmailText(User user) {
        return String.format("Hallo %s,\n\nDu hast dein Passwort vergessen? Nicht so schlimm, hier kannst du dir ein neues Passwort setzen: %s", FormatUtils.toFirstLastName(user.getPerson()), "http://localhost:8085/auth/redeemtoken");
    }


}
