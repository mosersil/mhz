package com.silviomoser.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import static com.silviomoser.demo.utils.StringUtils.isBlank;

@Slf4j
@Service
public class EmailSenderService {

    @Autowired
    public JavaMailSender javaMailSender;


    public void sendSimpleMail(String emailAddressFrom, String emailAddressTo, String emailSubject, String emailContentText, String senderName, String senderEmailAddress) throws ServiceException {

        if (isBlank(senderName) || isBlank(senderEmailAddress) || isBlank(emailContentText)) {
            throw new ServiceException("Missing/Invalid attributes");
        }

        if (EmailValidator.getInstance().isValid(senderEmailAddress)) {

            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            try {
                helper.setFrom(emailAddressFrom);
                helper.setTo(emailAddressTo);
                helper.setSubject(emailSubject);
                helper.setText(emailContentText);
                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                log.error("Exception while sending email", e);
                throw new ServiceException("Technical error occured");
            }
        } else {
            throw new ServiceException("Invalid email address");
        }
    }
}
