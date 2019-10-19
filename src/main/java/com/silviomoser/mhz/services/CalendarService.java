package com.silviomoser.mhz.services;

import biweekly.ICalendar;
import com.silviomoser.mhz.data.CalendarEvent;
import com.silviomoser.mhz.repository.CalendarEventRepository;
import com.silviomoser.mhz.utils.CalendarUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.silviomoser.mhz.utils.CalendarUtils.firstMomentOfYear;
import static com.silviomoser.mhz.utils.CalendarUtils.lastMomentOfYear;

@Service
@Slf4j
public class CalendarService extends AbstractCrudService<CalendarEvent> {

    @Autowired
    private CalendarEventRepository calendarEventRepository;


    @Cacheable("icalCalendar")
    public ICalendar getIcalCalendar(boolean publicOnly) {
        final LocalDateTime localDate = LocalDateTime.now().minusYears(1);
        List<CalendarEvent> calendarEventList = calendarEventRepository.findCalendarEventsFromStartDate(localDate);

        if (publicOnly) {
            List<CalendarEvent> filtered = calendarEventList.stream().filter(it -> it.isPublicEvent()).collect(Collectors.toList());
            calendarEventList = filtered;
        }

        final ICalendar ical = new ICalendar();

        calendarEventList.stream().map(CalendarUtils::toIcalEvent).forEach(ical::addEvent);
        return ical;
    }

    @Cacheable("eventsPerYear")
    public List<CalendarEvent> getAllEventsForCurrentYear(int year) {
        return calendarEventRepository.findCalendarEventsBetween(firstMomentOfYear(year), lastMomentOfYear(year));
    }

    public List<CalendarEvent> findByTitleLike(String title) throws ServiceException {
        return calendarEventRepository.findByTitleContains(title);
    }

    public int countByTitleLike(String title) throws ServiceException {
        return calendarEventRepository.countByTitleLike(title);
    }

    public List<CalendarEvent> findAllEntries(LocalDateTime from, boolean publicOnly, Integer maxResults) {
        List<CalendarEvent> foundItems = calendarEventRepository.findCalendarEventsFromStartDate(from);
        foundItems.sort(CalendarEvent::compareTo);

        if (publicOnly) {
            foundItems = foundItems.stream().filter(p -> p.isPublicEvent()).collect(Collectors.toList());
        }

        if (maxResults != null && foundItems.size() >= maxResults) {
            foundItems = foundItems.subList(0, maxResults);
        }
        log.debug("Returning {} items", foundItems.size());
        return foundItems;
    }

}
