package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.StaticFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticFileRepository extends JpaRepository<StaticFile, Long> {
}
