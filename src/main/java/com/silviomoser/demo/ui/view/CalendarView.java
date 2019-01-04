package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.CalendarEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static com.silviomoser.demo.utils.VaadinUtils.booleanToHtmlValueProvider;

/**
 * Created by silvio on 28.07.18.
 */

@SpringView(name = CalendarView.VIEW_NAME)
public class CalendarView extends AbstractCrudView<CalendarEvent> {
    public static final String VIEW_NAME = "calendar";



    @Override
    public void populateGrid(Grid<CalendarEvent> grid) {
        grid.addColumn(CalendarEvent::getId).setCaption("#").setWidth(70);

        grid.addColumn(CalendarEvent::getDateStart).setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.GERMAN))).setCaption("Datum/Zeit").setWidth(150);
        grid.addColumn(CalendarEvent::getTitle).setCaption("Anlass");

        grid.addColumn(CalendarEvent::isFullDay).setRenderer(booleanToHtmlValueProvider(), new HtmlRenderer()).setCaption("Ganztägig").setWidth(70);
        grid.addColumn(CalendarEvent::getRemarks).setCaption("Bemerkungen").setMaximumWidth(500);

    }

    @Override
    public CalendarEvent createNew() {
        return CalendarEvent.builder()
                .title("Neuer Anlass")
                .dateStart(LocalDateTime.now())
                .build();
    }

    @Override
    public boolean applyFilter(CalendarEvent s, String filterString) {
        return s.getTitle().contains(filterString);
    }


}

