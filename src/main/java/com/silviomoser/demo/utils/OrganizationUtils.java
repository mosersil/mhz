package com.silviomoser.demo.utils;

import com.silviomoser.demo.data.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrganizationUtils {

    public static List<String> getActiveOrganizations(Person person) {
        return person.getMemberships().stream().filter(membership -> (membership.getJoinDate().isBefore(LocalDate.now()) &&
                (membership.getLeaveDate() == null || membership.getLeaveDate().isAfter(LocalDate.now())))).map(membership -> membership.getOrganization().getName()).collect(Collectors.toList());
    }
}
