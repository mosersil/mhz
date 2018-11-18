package com.silviomoser.demo.services;

import com.silviomoser.demo.api.contact.EmailModel;
import com.silviomoser.demo.api.contact.EmailStatus;
import com.silviomoser.demo.config.ContactConfiguration;
import com.silviomoser.demo.ui.i18.I18Helper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by silvio on 25.08.18.
 */
@Service("emailService")

public class ContactService {

    I18Helper i18Helper = new I18Helper();

    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());

    @Autowired
    private ContactConfiguration contactConfiguration;


    @Autowired
    public JavaMailSender javaMailSender;


    //Todo: Needs refactoring...
    public EmailStatus sendSimpleMail(EmailModel emailModel) {
        final EmailStatus emailStatus = new EmailStatus();
        emailStatus.setSuccess(false);

        if (StringUtils.isEmpty(emailModel.getName()) || StringUtils.isEmpty(emailModel.getEmail()) || StringUtils.isEmpty(emailModel.getMessage())) {
            emailStatus.setErrorDetails(i18Helper.getMessage("contact_invalidentry"));
            return emailStatus;
        }

        if (emailModel.getMessage().length()>2000) {
            emailStatus.setErrorDetails(i18Helper.getMessage("contact_messagetoolong"));
            return emailStatus;
        }

        if (EmailValidator.getInstance().isValid(emailModel.getEmail())) {
            //Create message
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            try {
                helper.setFrom(contactConfiguration.getContactEmailFrom());
                helper.setTo(contactConfiguration.getContactEmailTo());
                helper.setSubject(contactConfiguration.getGetContactEmailSubject());
                helper.setText(emailModel.getName()+" ("+emailModel.getEmail()+")\n\n"+emailModel.getMessage());
                javaMailSender.send(message);
                emailStatus.setSuccess(true);
            } catch (Exception e) {
                LOGGER.log( Level.SEVERE, "Exception while sending email", e);
                emailStatus.setSuccess(false);
                emailStatus.setErrorDetails(i18Helper.getMessage("contact_technicalerror"));
            }
        } else {
            emailStatus.setErrorDetails(i18Helper.getMessage("contact_invalidemail"));
        }
        return emailStatus;
    }
}
