package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}
