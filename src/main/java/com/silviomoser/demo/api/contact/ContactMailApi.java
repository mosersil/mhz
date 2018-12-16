package com.silviomoser.demo.api.contact;

import com.captcha.botdetect.web.servlet.SimpleCaptcha;
import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.services.ContactService;
import com.silviomoser.demo.services.ServiceException;
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
public class ContactMailApi implements ApiController {

    @Autowired
    private ContactService contactService;


    @CrossOrigin(origins = "*")
    @RequestMapping(value = URL_CONTACT, method = RequestMethod.POST)
    public void sendEmail(HttpServletRequest request, @Valid @RequestBody contactFormModel contactFormModel) {

        try {
            contactService.sendContactForm(request,
                    contactFormModel.getName(),
                    contactFormModel.getEmail(),
                    contactFormModel.getMessage(),
                    contactFormModel.getCaptchaCode(),
                    contactFormModel.getCaptchaId());
        } catch (ServiceException e) {
            throw new ApiException(e.getMessage());
        }

        log.info("Contact form has been submitted by {}: {}", contactFormModel.getEmail(), contactFormModel.getMessage());
    }

}
