package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Organization;
import com.silviomoser.mhz.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrganizationService extends AbstractCrudService<Organization> {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization findByName(String name) {
        return  organizationRepository.findByName(name);
    }
}
