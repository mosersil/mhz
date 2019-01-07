package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.data.type.StaticFileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaticFileRepository extends JpaRepository<StaticFile, Long> {

    List<StaticFile> findByStaticFileCategoryOrderByCreatedDesc(StaticFileCategory staticFileCategory);
}
