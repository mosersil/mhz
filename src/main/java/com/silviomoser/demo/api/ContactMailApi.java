package com.silviomoser.demo.api;

import com.silviomoser.demo.services.EmailService;
import com.silviomoser.demo.data.EmailModel;
import com.silviomoser.demo.data.EmailStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by silvio on 20.08.18.
 */
@RestController
public class ContactMailApi {


    private static final Logger LOGGER = LoggerFactory.getLogger(ContactMailApi.class);

    @Autowired
    private EmailService emailService;


    @RequestMapping(value = "/api/contact", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public EmailStatus sendEmail(EmailModel emailModel) {
        return emailService.sendSimpleMail(emailModel);
    }
}
