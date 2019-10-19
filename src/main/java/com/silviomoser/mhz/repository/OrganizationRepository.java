package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by silvio on 13.10.18.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {


    Organization findByName(String name);


}
