package com.silviomoser.demo.security;

import com.silviomoser.demo.data.User;
import com.silviomoser.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(String.format("enter loadUserbyUsername(). Username='%s'", username));
        final Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Username Not Found Exception : " + username);
        }
        return new SecurityUserDetails(user.get());
    }
}
