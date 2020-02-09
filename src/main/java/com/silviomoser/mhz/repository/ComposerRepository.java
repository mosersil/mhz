package com.silviomoser.mhz.repository;


import com.silviomoser.mhz.data.Composer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface ComposerRepository extends JpaRepository<Composer, Long>, JpaSpecificationExecutor<Composer> {

    Collection<Composer> findByNameContainingIgnoreCase(String name);

    Composer findOneByName(String name);
}
