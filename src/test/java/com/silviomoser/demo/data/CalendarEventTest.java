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
                        CalendarEvent.builder().title("today").dateStart(LocalDateTime.now()).build(),
                        CalendarEvent.builder().title("yesterday").dateStart(LocalDateTime.now().minus(1, ChronoUnit.DAYS)).build(),
                        1
                },
                {
                        CalendarEvent.builder().title("yesterday").dateStart(LocalDateTime.now().minus(1, ChronoUnit.DAYS)).build(),
                        CalendarEvent.builder().title("today").dateStart(LocalDateTime.now()).build(),
                        -1
                },
                {

                        CalendarEvent.builder().title("today").dateStart(date).build(),
                        CalendarEvent.builder().title("today").dateStart(date).build(),
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
                .dateStart(date)
                .dressCode(dressCode)
                .advertise(advertise)
                .fullDay(fullday)
                .publicEvent(isPublic)
                .remarks(remarks)
                .build();

        assertThat(builtEvent.getDateStart()).isEqualTo(date);
    }

}