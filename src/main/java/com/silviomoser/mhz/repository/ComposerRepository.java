package com.silviomoser.mhz.repository;


import com.silviomoser.mhz.data.Composer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ComposerRepository extends JpaRepository<Composer, Long> {

    Collection<Composer> findByNameContainingIgnoreCase(String name);

    Composer findOneByName(String name);
}
