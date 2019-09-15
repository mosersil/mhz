package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.data.type.StaticFileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaticFileRepository extends JpaRepository<StaticFile, Long> {

    List<StaticFile> findByStaticFileCategoryOrderByCreatedDesc(StaticFileCategory staticFileCategory);
}
