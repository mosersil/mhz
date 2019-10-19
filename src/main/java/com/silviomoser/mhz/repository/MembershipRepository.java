package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Membership;
import com.silviomoser.mhz.data.Organization;
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

    @Query("SELECT m FROM Membership m where m.organization = :organization and m.leaveDate is null")
    List<Membership> findActiveMembersByOrganization(@Param("organization") Organization organization);

    @Query("SELECT m FROM Membership m join Person p where p.lastName = :lastName")
    List<Membership> findByPersonLastNameContains(@Param("lastName") String lastName);


}
