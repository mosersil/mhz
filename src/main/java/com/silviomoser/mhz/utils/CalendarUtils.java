package com.silviomoser.mhz.utils;

import biweekly.component.VEvent;
import com.silviomoser.mhz.data.CalendarEvent;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class CalendarUtils {


    public static VEvent toIcalEvent(CalendarEvent calendarEvent) {
        final VEvent event = new VEvent();
        if (calendarEvent.isFullDay()) {
            event.setDateStart(localDateToDate(calendarEvent.getDateStart()), false);
        } else {
            event.setDateStart(localDateToDate(calendarEvent.getDateStart()));
            event.setDateEnd(localDateToDate(calendarEvent.getDateEnd()));
        }
        event.setSummary(calendarEvent.getTitle());
        event.setDescription(calendarEvent.getRemarks());
        event.setLocation(calendarEvent.getLocation());
        return event;
    }

    private static Date localDateToDate(LocalDateTime localDateTime) {
        final ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }


    public static LocalDateTime firstMomentOfYear(int year) {
        return LocalDateTime.of(year, Month.JANUARY, 1, 0, 0, 0);
    }

    public static LocalDateTime lastMomentOfYear(int year) {
        return LocalDateTime.of(year, Month.DECEMBER, 31, 23,59,59);
    }
}
