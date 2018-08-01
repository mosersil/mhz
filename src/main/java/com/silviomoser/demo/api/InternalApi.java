package com.silviomoser.demo.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by silvio on 29.07.18.
 */
@RestController
public class InternalApi {

    @Autowired
    UserRepository userRepository;

    @JsonView(Views.Public.class)
    @RequestMapping("/internal/api/user")
    public Person my() {
        return SecurityUtils.getMy();
    }
}
