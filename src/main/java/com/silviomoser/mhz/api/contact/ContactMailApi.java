package com.silviomoser.mhz.api.contact;

import com.silviomoser.mhz.api.core.ApiController;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.services.ContactService;
import com.silviomoser.mhz.services.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


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
    public void sendEmail(HttpServletRequest request, @Valid @RequestBody ContactFormModel contactFormModel) {

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
