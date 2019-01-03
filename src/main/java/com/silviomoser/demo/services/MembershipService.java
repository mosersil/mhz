package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Membership;
import com.silviomoser.demo.data.Organization;
import com.silviomoser.demo.repository.MembershipRepository;
import com.silviomoser.demo.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {

    @Autowired
    MembershipRepository membershipRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    public List<Membership> getMemberList(String organizationName) {
        List<Membership> result = null;
        final Organization organization = organizationRepository.findByName(organizationName);

        if (organization != null) {
            result = membershipRepository.findActiveMembersByOrganization(organization);
        }

        return result;
    }
}


