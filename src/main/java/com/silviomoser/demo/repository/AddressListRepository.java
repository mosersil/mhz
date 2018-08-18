package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.AddressListEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by silvio on 16.08.18.
 */
public interface AddressListRepository extends JpaRepository<AddressListEntry, Long> {

    @Query("SELECT a FROM AddressListEntry a where a.organization = :organization")
    List<AddressListEntry> findByOrganization(@Param("organization") String organization);
}
