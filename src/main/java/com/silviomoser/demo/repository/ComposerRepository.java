package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Composer;
import com.silviomoser.demo.data.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ComposerRepository extends JpaRepository<Composer, Long> {

    Collection<Composition> findByNameContaining(String title);
}
