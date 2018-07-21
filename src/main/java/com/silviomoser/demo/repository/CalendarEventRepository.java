package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    @Query("SELECT e FROM CalendarEvent e WHERE e.date > :startFrom ")
    List<CalendarEvent> findCalendarEventsFromStartDate(@Param("startFrom") LocalDateTime startFrom);
}