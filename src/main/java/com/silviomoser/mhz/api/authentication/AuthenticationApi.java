package com.silviomoser.mhz.api.authentication;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.api.core.ApiController;
import com.silviomoser.mhz.api.core.ApiErrorDetail;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.User;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.security.utils.SecurityUtils;
import com.silviomoser.mhz.services.ChangePasswordException;
import com.silviomoser.mhz.services.PasswordService;
import com.silviomoser.mhz.services.ServiceException;
import com.silviomoser.mhz.ui.i18.I18Helper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
public class AuthenticationApi implements ApiController {


    @Autowired
    PasswordService passwordService;

    @Autowired
    I18Helper i18Helper;


    @ApiOperation(value = "Get details on the currently logged in user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Person.class),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @JsonView(Views.Internal.class)
    @RequestMapping(value = URL_AUTH_USER, method = RequestMethod.GET)
    public Person my() {
        final Person me = SecurityUtils.getMe();
        if (me == null) {
            throw new ApiException("Not authorized", HttpStatus.UNAUTHORIZED);
        }
        log.debug(String.format("my() returns '%s'", me));
        return me;
    }


    @ApiOperation(value = "Start password reset process")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Technical error")
    })
    @RequestMapping(value = URL_AUTH_INITPWRESET, method = RequestMethod.POST)
    public void initPwReset(Locale locale, @RequestBody ResetPasswordForm resetPasswordForm) {
        log.debug("enter initPwReset: " + resetPasswordForm);
        try {
            passwordService.startPwResetSelfService(resetPasswordForm.getEmail(), resetPasswordForm.getForward());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(i18Helper.getMessage(i18Helper.getMessage("generic_techerror")), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Redeem a change password token")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Technical error")
    })
    @RequestMapping(value = URL_AUTH_REDEEMTOKEN, method = RequestMethod.POST)
    public void redeemToken(@RequestBody RedeemTokenForm redeemTokenForm) {
        try {
            final User user = passwordService.redeemToken(redeemTokenForm.getToken(), redeemTokenForm.getPassword(), redeemTokenForm.getPassword_confirmation());
            log.info("User {} has successfully updated password", user.getUsername());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(i18Helper.getMessage("pwreset_exception_passwordrules"));
        }
    }

    @ApiOperation(value = "Change password of an authenticated user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Technical error")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = URL_AUTH_CHANGEPW, method = RequestMethod.POST)
    public void changePassword(@RequestBody ChangePasswordForm changePasswordForm) {
        try {
            passwordService.changePassword(changePasswordForm.getCurrentPassword(), changePasswordForm.getNewPassword(), changePasswordForm.getConfirmPassword());
        } catch (ServiceException e) {
            if (e instanceof ChangePasswordException) {
                ChangePasswordException cpe = (ChangePasswordException) e;
                log.warn(cpe.getMessage());
                throw new ApiException(e.getLocalizedMessage(), HttpStatus.OK, ApiErrorDetail.builder().errorContext(cpe.getContext()).errorMessage(cpe.getLocalizedMessage()).build());
            }
            log.error(e.getMessage(), e);
            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
