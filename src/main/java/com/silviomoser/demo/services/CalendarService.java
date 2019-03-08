package com.silviomoser.demo.services;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.repository.CalendarEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
@Slf4j
public class CalendarService {

    @Autowired
    private CalendarEventRepository calendarEventRepository;


    public ICalendar getIcalCalendar(boolean publicOnly) {
        final LocalDateTime localDate = LocalDateTime.now().minusYears(1);
        List<CalendarEvent> calendarEventList = calendarEventRepository.findCalendarEventsFromStartDate(localDate);

        if (publicOnly) {
            List<CalendarEvent> filtered = calendarEventList.stream().filter(it -> it.isPublicEvent()).collect(Collectors.toList());
            calendarEventList = filtered;
        }

        final ICalendar ical = new ICalendar();

        calendarEventList.forEach(it -> {
            final VEvent event = new VEvent();
            if (it.isFullDay()) {
                event.setDateStart(localDateToDate(it.getDateStart()), false);
            } else {
                event.setDateStart(localDateToDate(it.getDateStart()));
                event.setDateEnd(localDateToDate(it.getDateEnd()));
            }
            event.setSummary(it.getTitle());
            event.setDescription(it.getRemarks());
            event.setLocation(it.getLocation());
            ical.addEvent(event);
        });
        return ical;
    }

    public List<CalendarEvent> getAllEventsForCurrentYear(int year) {
        final LocalDate now = LocalDate.now();
        final LocalDate from = LocalDate.of(year, 1, 1);
        final LocalDate to = from.with(lastDayOfYear());
        return calendarEventRepository.findCalendarEventsBetween(from.atStartOfDay(), to.atTime(23, 59));
    }


    private Date localDateToDate(LocalDateTime localDateTime) {
        final ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public CalendarEvent addOrUpdate(CalendarEvent calendarEvent) throws ServiceException {
        final CalendarEvent calendarEvent1 = calendarEventRepository.save(calendarEvent);
        log.info("Saved article {}", calendarEvent1.getId());
        return calendarEvent1;
    }

    public void delete(CalendarEvent calendarEvent) throws ServiceException {
        calendarEventRepository.delete(calendarEvent);
        log.info("Deleted calendarEvent {} " + calendarEvent.getTitle());
    }

    public List<CalendarEvent> findByTitleLike(String title) throws ServiceException {
        return calendarEventRepository.findByTitleContains(title);
    }

    public int countByTitleLike(String title) throws ServiceException {
        return calendarEventRepository.countByTitleLike(title);
    }

}
