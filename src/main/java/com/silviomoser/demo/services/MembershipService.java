package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Membership;
import com.silviomoser.demo.data.Organization;
import com.silviomoser.demo.repository.MembershipRepository;
import com.silviomoser.demo.repository.OrganizationRepository;
import com.silviomoser.demo.utils.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MembershipService {

    private static final String CACHE_MEMBERLIST = "memberlist";

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private CacheManagerService cacheManagerService;

    @Cacheable(CACHE_MEMBERLIST)
    public List<Membership> getMemberList(String organizationName) {
        List<Membership> result = null;
        final Organization organization = organizationRepository.findByName(organizationName);

        if (organization != null) {
            result = membershipRepository.findActiveMembersByOrganization(organization);
        }

        return result;
    }

    public void delete(Membership membership) throws ServiceException {
        membershipRepository.delete(membership);
        cacheManagerService.clearCache(CACHE_MEMBERLIST);
        log.info("Deleted membership {}", FormatUtils.toFirstLastName(membership.getPerson()));
    }

    public Membership addOrUpdate(Membership membership) throws ServiceException {
        Membership membership1 = membershipRepository.save(membership);
        cacheManagerService.clearCache(CACHE_MEMBERLIST);
        log.info("Saved membership {}", FormatUtils.toFirstLastName(membership.getPerson()));
        return membership1;
    }

    public List<Membership> findAllByOrganization(Organization organization) throws ServiceException {
        return membershipRepository.findByOrganization(organization);
    }

    public List<Membership> findAllByOrganization(Organization organization, String name) throws ServiceException {
        return membershipRepository.findByOrganization(organization).stream().filter(it -> it.getPerson().getLastName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

}


