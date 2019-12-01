package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  SheetRepository extends JpaRepository<Sheet, Long> {
}
