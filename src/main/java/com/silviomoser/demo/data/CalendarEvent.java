package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.utils.PdfReport;
import com.silviomoser.demo.utils.XlsReport;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

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
    @JsonView(Views.Public.class)
    @PdfReport(header = "Datum")
    @XlsReport(header = "Beginn")
    @Column(name = "DATE_START")
    private LocalDateTime dateStart;


    @NotNull
    @JsonView(Views.Public.class)
    @Column(name = "DATE_END")
    @XlsReport(header = "Ende")
    private LocalDateTime dateEnd;

    @NotNull
    @JsonView(Views.Public.class)
    @Column(name = "TITLE")
    @PdfReport(header = "Anlass")
    @XlsReport(header = "Anlass")
    private String title;

    @JsonView(Views.Public.class)
    @Column(name = "FULL_DAY")
    @XlsReport(header = "Ganztägig")
    private boolean fullDay;

    @JsonView(Views.Public.class)
    @Column(name = "PUBLIC_EVENT")
    @XlsReport(header = "Öffentlich")
    private boolean publicEvent;

    @JsonView(Views.Public.class)
    @Column(name="ADVERTISE")
    private boolean advertise;

    @JsonView(Views.Public.class)
    @PdfReport(header = "Bemerkungen")
    @XlsReport(header = "Bemerkungen")
    @Column(name = "REMARKS")
    private String remarks;

    @JsonView(Views.Public.class)
    @PdfReport(header = "Ort")
    @XlsReport(header = "Ort")
    @Column(name = "LOCATION")
    private String location;

    @JsonView(Views.Public.class)
    @Column(name="DRESS_CODE")
    @PdfReport(header = "Tenue")
    @XlsReport(header = "Tenue")
    private DressCode dressCode;

    @ToString.Exclude
    @JsonView(Views.Public.class)
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<StaticFile> files;

    @ToString.Exclude
    @OneToMany(mappedBy = "event")
    private Set<Participation> participants;

    @Override
    public int compareTo(CalendarEvent o) {
            return this.getDateStart().compareTo(o.getDateStart());
    }
}
