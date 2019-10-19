package com.silviomoser.mhz.api.internal;

import lombok.Data;

import java.time.LocalDate;

import static java.lang.Integer.parseInt;

@Data
public class BirthdayForm {
    private String year;
    private String month;
    private String day;

    public String toString() {
        return year+"-"+month+"-"+day;
    }

    public LocalDate getDate() {
        return LocalDate.of(parseInt(year), parseInt(month), parseInt(day));
    }
}
