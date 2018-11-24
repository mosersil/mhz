package com.silviomoser.demo.utils;

import com.silviomoser.demo.data.Membership;
import com.silviomoser.demo.data.Organization;
import com.silviomoser.demo.data.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrganizationUtils {

    public static List<String> getActiveOrganizations(Person person) {
        return person.getMemberships().stream().filter(membership -> (membership.getJoinDate().isBefore(LocalDateTime.now()) &&
                (membership.getLeaveDate() == null || membership.getLeaveDate().isAfter(LocalDateTime.now())))).map(membership -> membership.getOrganization().getName()).collect(Collectors.toList());
    }
}
