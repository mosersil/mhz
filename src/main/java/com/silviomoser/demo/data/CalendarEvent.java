package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.utils.PdfReport;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "CALENDAR_EVENT")
public class CalendarEvent extends AbstractEntity implements Comparable<CalendarEvent>{


    @NotNull
    @PdfReport(header = "Datum")
    @Column(name = "DATE")
    private LocalDateTime date;

    @NotNull
    @Column(name = "TITLE")
    @PdfReport(header = "Anlass")
    private String title;

    @Column(name = "FULL_DAY")
    private boolean fullDay;

    @Column(name = "PUBLIC_EVENT")
    private boolean publicEvent;

    @Column(name="ADVERTISE")
    private boolean advertise;

    @PdfReport(header = "Bemerkungen")
    @Column(name = "REMARKS")
    private String remarks;

    @Column(name="DRESS_CODE")
    @PdfReport(header = "Tenue")
    private DressCode dressCode;

    @Override
    public int compareTo(CalendarEvent o) {
            return this.getDate().compareTo(o.getDate());
    }
}
