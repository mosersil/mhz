package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Membership;
import com.silviomoser.mhz.data.Organization;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.repository.MembershipRepository;
import com.silviomoser.mhz.repository.OrganizationRepository;
import com.silviomoser.mhz.utils.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MembershipService extends AbstractCrudService<Membership> {

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

    public Membership addNewMember(Person p, String organizationName, String function, String remarks) throws ServiceException {
        final Organization organization = organizationRepository.findByName(organizationName);

        if (organization!=null) {
            final Membership membership = new Membership();
            membership.setOrganization(organization);
            membership.setPerson(p);
            membership.setJoinDate(LocalDate.now());
            membership.setFunction(function);
            membership.setRemarks(remarks);
            return membershipRepository.save(membership);
        }
        else {
            throw new ServiceException("Could not find organization with name " + organizationName);
        }
    }

}


