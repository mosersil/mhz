package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
