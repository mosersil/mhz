package com.silviomoser.demo.api.contact;

import com.silviomoser.demo.services.ContactService;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.silviomoser.demo.utils.TtlMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;


/**
 * Created by silvio on 20.08.18.
 */
@Slf4j
@RestController
public class ContactMailApi {

    @Autowired
    private ContactService contactService;

    @Autowired
            private I18Helper i18Helper;

    final TtlMap<String, String> hitCache = new TtlMap<>(TimeUnit.SECONDS, 10);

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/api/contact", method = RequestMethod.POST)
    public EmailStatus sendEmail(HttpServletRequest request, @Valid @RequestBody EmailModel emailModel) {

        EmailStatus emailStatus = new EmailStatus();

        if (hitCache.get(request.getRemoteAddr())==null) {
            hitCache.put(request.getRemoteAddr(), request.getRemoteAddr());
            emailStatus = contactService.sendSimpleMail(emailModel);
        } else {
            log.warn("Too many hits detected by {}", request.getRemoteAddr());
            emailStatus.setSuccess(false);
            emailStatus.setErrorDetails(i18Helper.getMessage("contact_toomanyrequests"));
        }
        log.info("Contact form has been submitted by {}: {}", emailModel.getEmail(), emailModel.getMessage());
        return emailStatus;
    }

}
