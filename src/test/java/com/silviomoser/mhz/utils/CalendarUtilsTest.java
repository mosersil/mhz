package com.silviomoser.mhz.utils;


import biweekly.component.VEvent;
import com.silviomoser.mhz.data.CalendarEvent;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CalendarUtilsTest {


    @DataProvider(name = "testToIcalEventDp")
    public Object[][] testToIcalEventDp() {
        return new Object[][] {
                {build("Test1", LocalDateTime.now(), LocalDateTime.now())}
        };
    }

    @Test(dataProvider = "testToIcalEventDp")
    public void testToIcalEvent(CalendarEvent calendarEvent) {
        VEvent vEvent = CalendarUtils.toIcalEvent(calendarEvent);
        assertThat(vEvent.getUrl()).isNull();
        assertThat(vEvent.getSummary().getValue()).isEqualTo(calendarEvent.getTitle());

        ZonedDateTime startZonedDateTime = calendarEvent.getDateStart().atZone(ZoneId.systemDefault());
        assertThat(vEvent.getDateStart().getValue().getTime()).isEqualTo(startZonedDateTime.toInstant().toEpochMilli());

        ZonedDateTime endZonedDateTime = calendarEvent.getDateStart().atZone(ZoneId.systemDefault());
        assertThat(vEvent.getDateStart().getValue().getTime()).isEqualTo(endZonedDateTime.toInstant().toEpochMilli());
    }

    private CalendarEvent build(String title, LocalDateTime dateStart, LocalDateTime dateEnd) {
        return CalendarEvent.builder()
                .title(title)
                .dateStart(dateStart)
                .dateEnd(dateEnd)
                .build();
    }

}