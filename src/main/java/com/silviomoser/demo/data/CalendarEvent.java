package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.DressCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class CalendarEvent implements Comparable<CalendarEvent>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime date;
    private String title;
    private boolean fullDay;
    private boolean publicEvent;
    private boolean advertise;
    private String remarks;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
