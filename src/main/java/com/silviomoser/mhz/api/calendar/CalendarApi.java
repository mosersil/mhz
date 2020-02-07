package com.silviomoser.mhz.api.calendar;

import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.CalendarEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping(value = CalendarApi.API_CONTEXTROOT)
public class CalendarApi extends CrudApi<CalendarEvent> {

    public static final String API_CONTEXTROOT = "/api/calendar";


}
