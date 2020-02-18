package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.AddressListEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by silvio on 16.08.18.
 */
public interface AddressListRepository extends JpaRepository<AddressListEntry, Long>, JpaSpecificationExecutor<AddressListEntry> {

    @Query("SELECT a FROM AddressListEntry a where a.organization in :organization")
    List<AddressListEntry> findByOrganization(@Param("organization") List<String> organization);

}
