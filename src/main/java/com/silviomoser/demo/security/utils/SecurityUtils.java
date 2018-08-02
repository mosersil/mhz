package com.silviomoser.demo.security.utils;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.security.SecurityUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by silvio on 27.05.18.
 */
public class SecurityUtils {

    public static final String ANONYMOUS = "Anonymous";


    public static Person getMy() {
        SecurityUserDetails securityUserDetails = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth instanceof AnonymousAuthenticationToken)) {
                securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            }
        }
        Person my = null;
        if (securityUserDetails!=null) {
            my = securityUserDetails.getPerson();
        }

        return my;
    }

    public static String getLoggedInUserFullName() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth instanceof AnonymousAuthenticationToken)) {
                SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                stringBuilder.append(userDetails.getPerson().getFirstName())
                        .append(" ")
                        .append(userDetails.getPerson().getLastName());
            } else {
                stringBuilder.append(ANONYMOUS);
            }
        } else {
            stringBuilder.append(ANONYMOUS);
        }
        return stringBuilder.toString();
    }


    public static Set<String> getGroups() {
        final HashSet<String> groups = new HashSet<>();
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth instanceof AnonymousAuthenticationToken)) {
                SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                userDetails.getPerson().getMemberships().forEach(it -> groups.add(it.getOrganization().getName()));
            }

        }
        return groups;
    }


}
