package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Membership;
import com.silviomoser.demo.data.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by silvio on 10.10.18.
 */
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    @Query("SELECT m FROM Membership m where m.organization = :organization")
    List<Membership> findByOrganization(@Param("organization") Organization organization);


}
