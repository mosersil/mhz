package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {

    List<Composition> findByTitleContainingIgnoreCase(String title);


    List<Composition> findByComposersNameIsContainingIgnoreCase(String composerSearchTerm);
}
