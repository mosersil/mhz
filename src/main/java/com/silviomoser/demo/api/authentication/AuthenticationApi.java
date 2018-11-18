package com.silviomoser.demo.api.authentication;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.services.ContactService;
import com.silviomoser.demo.services.PasswordResetService;
import com.silviomoser.demo.services.ServiceException;
import com.silviomoser.demo.ui.i18.I18Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
public class AuthenticationApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactService contactService;

    @Autowired
    PasswordResetService passwordResetService;

    @Autowired
    I18Helper i18Helper;


    @JsonView(Views.Public.class)
    @RequestMapping("/auth/user")
    public Person my() {
        final Person me = SecurityUtils.getMe();
        if (me == null) {
            throw new ApiException("Not authorized", HttpStatus.UNAUTHORIZED);
        }
        log.debug(String.format("my() returns '%s'", me));
        return me;
    }


    @JsonView(Views.Public.class)
    @RequestMapping(value = "/auth/initPwReset", method = RequestMethod.POST)
    public void initPwReset(Locale locale, @RequestBody ResetPasswordForm resetPasswordForm) {
        log.debug("enter initPwReset: " + resetPasswordForm);
        try {
            passwordResetService.startPwReset(resetPasswordForm.getEmail());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(i18Helper.getMessage(i18Helper.getMessage("generic_techerror")), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/auth/redeemToken", method = RequestMethod.GET)
    public void redeemToken(@RequestParam String token) {
        try {
            User user = passwordResetService.redeemToken(token);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        }
    }
}
