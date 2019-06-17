package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {

    List<Composition> findByTitleContaining(String title);
}
