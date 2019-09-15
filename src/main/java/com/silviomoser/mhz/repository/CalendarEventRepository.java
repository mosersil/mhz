package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    @Query("SELECT e FROM CalendarEvent e WHERE e.dateStart > :startFrom order by e.dateStart asc")
    List<CalendarEvent> findCalendarEventsFromStartDate(@Param("startFrom") LocalDateTime startFrom);

    @Query("SELECT e FROM CalendarEvent e WHERE e.dateStart > :start and e.dateStart < :until order by e.dateStart asc ")
    List<CalendarEvent> findCalendarEventsBetween(@Param("start") LocalDateTime start, @Param("until") LocalDateTime until);

    List<CalendarEvent> findByTitleContains(String title);

    int countByTitleLike(String title);
}
