package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.utils.PdfReport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "CALENDAR_EVENT")
public class CalendarEvent extends AbstractEntity implements Comparable<CalendarEvent>{


    @NotNull
    @PdfReport(header = "Datum")
    @Column(name = "DATE")
    private LocalDateTime date;

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

    public CalendarEvent(String title, LocalDateTime date) {
        this.title=title;
        this.date=date;
    }

    public CalendarEvent() {
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFullDay() {
        return fullDay;
    }

    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }

    public boolean isPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(boolean publicEvent) {
        this.publicEvent = publicEvent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isAdvertise() {
        return advertise;
    }

    public void setAdvertise(boolean advertise) {
        this.advertise = advertise;
    }

    public DressCode getDressCode() {
        return dressCode;
    }

    public void setDressCode(DressCode dressCode) {
        this.dressCode = dressCode;
    }

    @Override
    public int compareTo(CalendarEvent o) {
            return this.getDate().compareTo(o.getDate());
    }
}
