package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
