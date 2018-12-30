package com.silviomoser.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import static com.silviomoser.demo.utils.StringUtils.isBlank;
import static com.silviomoser.demo.utils.StringUtils.isValidEmailAddress;

@Slf4j
@Service
public class EmailSenderService {

    @Autowired
    public JavaMailSender javaMailSender;


    public void sendSimpleMail(String emailAddressFrom, String emailAddressTo, String emailSubject, String emailContentText) throws ServiceException {

        if (isBlank(emailAddressTo) || isBlank(emailAddressFrom) || isBlank(emailContentText) || isBlank(emailSubject)) {
            log.warn("Invalid attributes: from='{}', to='{}', subject='{}'", emailAddressFrom, emailAddressTo, emailSubject);
            throw new ServiceException("Missing/Invalid attributes");
        }

        if (!isValidEmailAddress(emailAddressFrom)) {
            log.warn("Invalid email address {}", emailAddressFrom);
            throw new ServiceException(String.format("Invalid FROM email address '{}'", emailAddressFrom));
        }
        if (!isValidEmailAddress(emailAddressTo)) {
            log.warn("Invalid email address {}", emailAddressTo);
            throw new ServiceException(String.format("Invalid TO email address '{}'", emailAddressTo));
        }


        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom(emailAddressFrom);
            helper.setTo(emailAddressTo);
            helper.setSubject(emailSubject);
            helper.setText(emailContentText);
            javaMailSender.send(mimeMessage);
            log.debug(String.format("Sent email to %s", emailAddressTo));
        } catch (Exception e) {
            log.error("Exception while sending email", e);
            throw new ServiceException("Technical error occured", e);
        }
    }

}
