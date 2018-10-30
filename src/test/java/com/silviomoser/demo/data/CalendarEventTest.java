package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.DressCode;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CalendarEventTest {

    @DataProvider(name = "testCompareToDp")
    public Object[][] testCompareToDp() {
        LocalDateTime date = LocalDateTime.now();
        return new Object[][]{
                {
                        CalendarEvent.builder().title("today").date(LocalDateTime.now()).build(),
                        CalendarEvent.builder().title("yesterday").date(LocalDateTime.now().minus(1, ChronoUnit.DAYS)).build(),
                        1
                },
                {
                        CalendarEvent.builder().title("yesterday").date(LocalDateTime.now().minus(1, ChronoUnit.DAYS)).build(),
                        CalendarEvent.builder().title("today").date(LocalDateTime.now()).build(),
                        -1
                },
                {

                        CalendarEvent.builder().title("today").date(date).build(),
                        CalendarEvent.builder().title("today").date(date).build(),
                        0
                }

        };
    }

    @Test(dataProvider = "testCompareToDp")
    public void testCompareTo(CalendarEvent event1, CalendarEvent event2, int expectedResult) {
        assertThat(event1.compareTo(event2)).isEqualTo(expectedResult);

    }

    @Test
    public void testBuild() {


        String title = "Test";
        LocalDateTime date = LocalDateTime.now();
        DressCode dressCode = DressCode.CASUAL;
        boolean advertise = true;
        boolean fullday = true;
        boolean isPublic = true;
        String remarks = "Test remarks";


        CalendarEvent builtEvent = CalendarEvent.builder()
                .title(title)
                .date(date)
                .dressCode(dressCode)
                .advertise(advertise)
                .fullDay(fullday)
                .publicEvent(isPublic)
                .remarks(remarks)
                .build();

        assertThat(builtEvent.getDate()).isEqualTo(date);
    }

    private CalendarEvent buildCalendarEvent(long id, String title, LocalDateTime date, DressCode dressCode, boolean advertise, boolean fullday, boolean isPublic, String remarks) {
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setTitle(title);
        calendarEvent.setDate(date);
        calendarEvent.setDressCode(dressCode);
        calendarEvent.setAdvertise(advertise);
        calendarEvent.setFullDay(fullday);
        calendarEvent.setPublicEvent(isPublic);
        calendarEvent.setRemarks(remarks);
        calendarEvent.setId(id);
        return calendarEvent;
    }
}