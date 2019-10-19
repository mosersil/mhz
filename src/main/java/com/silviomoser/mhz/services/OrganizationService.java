package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Organization;
import com.silviomoser.mhz.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public Organization findByName(String name) {
        return  organizationRepository.findByName(name);
    }
}
