package com.silviomoser.mhz.api.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import com.silviomoser.mhz.services.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CalendarDownload {

    @Autowired
    CalendarService calendarService;



    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/downloads/calendar/ical", produces = "text/calendar", method = RequestMethod.GET)
    public String calendarDownload(@RequestParam(name = "publicOnly", required = false, defaultValue = "false") boolean publicOnly) {
        final ICalendar calendar = calendarService.getIcalCalendar(publicOnly);
        return Biweekly.write(calendar).go();
    }
}
