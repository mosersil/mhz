package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Repertoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepertoireRepository extends JpaRepository<Repertoire, Long> {

    Repertoire findOneByName(String title);

    @Query("SELECT r FROM Repertoire r WHERE CURRENT_DATE BETWEEN r.dateStart AND r.dateEnd")
    List<Repertoire> findCurrent();


}
