package com.silviomoser.demo.api;

import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.repository.CalendarEventRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
public class CalendarApi {

    @Autowired
    CalendarEventRepository repository;


    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List events starting from a given date")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CalendarEvent.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/public/api/calendar", method = RequestMethod.GET)
    public List<CalendarEvent> list(@RequestParam(name = "max", required = false) Integer max,
                                    @RequestParam(name = "startFrom", required = false) String startFrom) {

        LocalDateTime startFromDate;
        if (startFrom == null || startFrom.isEmpty()) {
            startFromDate = LocalDate.now().atStartOfDay();
        } else {
            try {
                startFromDate = LocalDate.parse(startFrom).atStartOfDay();
            } catch (DateTimeParseException dtpe) {
                throw new CalendarApiException(dtpe.getMessage(), dtpe);
            }

        }


        List<CalendarEvent> all = repository.findCalendarEventsFromStartDate(startFromDate);
        all.sort(CalendarEvent::compareTo);

        if (max != null && all.size() >= max) {
            return all.subList(0, max);
        }
        return all;

    }

}
