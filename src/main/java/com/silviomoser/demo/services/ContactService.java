package com.silviomoser.demo.services;

import com.captcha.botdetect.web.servlet.SimpleCaptcha;
import com.silviomoser.demo.api.contact.contactFormModel;
import com.silviomoser.demo.api.contact.EmailStatus;
import com.silviomoser.demo.config.ContactConfiguration;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.silviomoser.demo.utils.TtlMap;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.silviomoser.demo.utils.StringUtils.isBlank;

/**
 * Created by silvio on 25.08.18.
 */
@Service("emailService")
@Slf4j
public class ContactService {

    @Autowired
    private I18Helper i18Helper;

    final TtlMap<String, String> hitCache = new TtlMap<>(TimeUnit.SECONDS, 10);

    @Autowired
    private ContactConfiguration contactConfiguration;

    @Autowired
    private EmailSenderService emailSenderService;


    @Autowired
    public JavaMailSender javaMailSender;

    public void sendContactForm(HttpServletRequest request, String name, String emailAddress, String message, String captchaCode, String captchaId) throws ServiceException {
        final SimpleCaptcha captcha = SimpleCaptcha.load(request);
        if (!captcha.validate(captchaCode, captchaId)) {
            throw new ServiceException("Captcha check failed");
        }

        if (hitCache.get(request.getRemoteAddr()) == null) {
            hitCache.put(request.getRemoteAddr(), request.getRemoteAddr());
            emailSenderService.sendSimpleMail(
                    contactConfiguration.getContactEmailFrom(),
                    contactConfiguration.getContactEmailTo(),
                    contactConfiguration.getGetContactEmailSubject(),
                    assembleEmail(name, emailAddress, message),
                    name,
                    emailAddress
                    );
        } else {
            log.warn("Too many hits detected by {}", request.getRemoteAddr());
            throw new ServiceException(i18Helper.getMessage("contact_toomanyrequests"));
        }
    }


    protected String assembleEmail(String name, String email, String message) {
        return new StringBuilder()
                .append(String.format("%s (%s) schrieb am %s:", name, email, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))))
                .append("\n\n")
                .append(message)
                .append("\n")
                .toString();
    }
}
