package com.silviomoser.demo.api.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateTimeStamp;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.services.CalendarService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by silvio on 10.05.18.
 */
@RestController
@Slf4j
public class CalendarApi {

    @Autowired
    CalendarEventRepository repository;

    @Autowired
    CalendarService calendarService;


    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List events starting from a given date")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CalendarEvent.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/public/api/calendar", method = RequestMethod.GET)
    public List<CalendarEvent> list(@RequestParam(name = "max", required = false) Integer max,
                                    @RequestParam(name = "startFrom", required = false) String startFrom,
                                    @RequestParam(name = "publicOnly", required = false, defaultValue = "false") boolean publicOnly) {

        LocalDateTime startFromDate;
        if (startFrom == null || startFrom.isEmpty()) {
            startFromDate = LocalDate.now().atStartOfDay();
        } else {
            try {
                startFromDate = LocalDate.parse(startFrom).atStartOfDay();
            } catch (DateTimeParseException dtpe) {
                throw new ApiException(dtpe.getMessage(), dtpe);
            }

        }

        List<CalendarEvent> foundItems = repository.findCalendarEventsFromStartDate(startFromDate);
        foundItems.sort(CalendarEvent::compareTo);

        if (publicOnly) {
            foundItems = foundItems.stream().filter(p -> p.isPublicEvent()).collect(Collectors.toList());
        }

        if (max != null && foundItems.size() >= max) {
            foundItems = foundItems.subList(0, max);
        }
        log.debug("Returning {} items", foundItems.size());
        return foundItems;

    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/public/api/ical", produces = "text/calendar", method = RequestMethod.GET)
    public String calendarDownload(@RequestParam(name = "publicOnly", required = false, defaultValue = "false") boolean publicOnly) {
        final ICalendar calendar = calendarService.getSubscribeCalendarEvents(publicOnly);
        return Biweekly.write(calendar).go();
    }



}
