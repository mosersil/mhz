package com.silviomoser.mhz.api.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.api.core.ApiController;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.data.CalendarEvent;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.services.CalendarService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Created by silvio on 10.05.18.
 */
@RestController
@Slf4j
public class CalendarApi implements ApiController {



    @Autowired
    CalendarService calendarService;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List events starting from a given date")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CalendarEvent.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_CALENDAR, method = RequestMethod.GET)
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
        return calendarService.findAllEntries(startFromDate, publicOnly, max);

    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/public/api/ical", produces = "text/calendar", method = RequestMethod.GET)
    public String calendarDownload(@RequestParam(name = "publicOnly", required = false, defaultValue = "false") boolean publicOnly) {
        final ICalendar calendar = calendarService.getIcalCalendar(publicOnly);
        return Biweekly.write(calendar).go();
    }



}
