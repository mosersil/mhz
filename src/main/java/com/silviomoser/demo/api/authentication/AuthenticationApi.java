package com.silviomoser.demo.api.authentication;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.security.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationApi {
    @JsonView(Views.Public.class)
    @RequestMapping("/auth/user")
    public Person my() {
        final Person me = SecurityUtils.getMe();
        if (me == null) {
            throw new ApiException("Not authorized", HttpStatus.UNAUTHORIZED);
        };
        return me;
    }
}
