package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by silvio on 13.10.18.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("SELECT o FROM Organization o where o.name = :name")
    Organization findByName(@Param("name") String name);

}
