package com.silviomoser.demo.api.authentication;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.api.contact.EmailModel;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class AuthenticationApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;


    @RequestMapping("/auth/user")
    public Person my() {
        final Person me = SecurityUtils.getMe();
        if (me == null) {
            throw new ApiException("Not authorized", HttpStatus.UNAUTHORIZED);
        };
        return me;
    }


    @JsonView(Views.Public.class)
    @RequestMapping("/auth/initPwReset")
    public void initPwReset(@RequestParam(name = "username", required = true) String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            user.setResetToken("randomToken");
            userRepository.save(user);

            EmailModel emailModel = new EmailModel();
            emailModel.setEmail(user.getPerson().getEmail());
            emailModel.setName("");
            //TODO: That wont work
            emailService.sendSimpleMail(emailModel);

        } else {
            log.warn("User {} does not exist");
            throw new ApiException("Invalid userId", HttpStatus.BAD_REQUEST);
        }
    }
}
