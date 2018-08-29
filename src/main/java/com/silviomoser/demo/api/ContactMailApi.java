package com.silviomoser.demo.api;

import com.silviomoser.demo.services.EmailService;
import com.silviomoser.demo.data.EmailModel;
import com.silviomoser.demo.data.EmailStatus;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.silviomoser.demo.utils.TtlMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;


/**
 * Created by silvio on 20.08.18.
 */
@RestController
public class ContactMailApi {



    private static final Logger LOGGER = LoggerFactory.getLogger(ContactMailApi.class);

    @Autowired
    private EmailService emailService;

    @Autowired
            private I18Helper i18Helper;

    TtlMap<String, String> hitCache = new TtlMap<>(TimeUnit.SECONDS, 10);


    @RequestMapping(value = "/api/contact", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public EmailStatus sendEmail(HttpServletRequest request, @Valid EmailModel emailModel) {

        EmailStatus emailStatus = new EmailStatus();

        if (hitCache.get(request.getRemoteAddr())==null) {
            hitCache.put(request.getRemoteAddr(), request.getRemoteAddr());
            emailStatus =  emailService.sendSimpleMail(emailModel);
        } else {
            emailStatus.setSuccess(false);
            emailStatus.setErrorDetails(i18Helper.getMessage("contact_toomanyrequests"));
        }
        return emailStatus;
    }
}
