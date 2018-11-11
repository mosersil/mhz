package com.silviomoser.demo.api.authentication;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.security.utils.SecurityUtils;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationApi {
    @JsonView(Views.Public.class)
    @CrossOrigin(origins = "*")
    @RequestMapping("/auth/user")
    public Person my() {
        final Person me = SecurityUtils.getMe();
        if (me == null) {
            //Person p = new Person();
            //p.setFirstName("Silvio");
            //p.setLastName("Moser");
            //return p;
            throw new ApiException("Not authorized", HttpStatus.UNAUTHORIZED);
        }
        log.debug(String.format("my() returns '%s'", me ));
        return me;
    }
}
