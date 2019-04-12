package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.utils.PdfReport;
import com.silviomoser.demo.utils.XlsReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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
    @PdfReport(header = "Ort")
    @XlsReport(header = "Ort")
    @Column(name = "LOCATION")
    private String location;

    @JsonView(Views.Public.class)
    @XlsReport(header = "Bemerkungen")
    @Column(name = "REMARKS")
    private String remarks;

    @JsonView(Views.Public.class)
    @Column(name="DRESS_CODE")
    @PdfReport(header = "Tenue")
    @XlsReport(header = "Tenue")
    @NotNull
    private DressCode dressCode;

    @ToString.Exclude
    @JsonView(Views.Public.class)
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<StaticFile> files;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @ToString.Exclude
    @OneToMany(mappedBy = "event")
    private Set<Participation> participants;

    @Override
    public int compareTo(CalendarEvent o) {
            return this.getDateStart().compareTo(o.getDateStart());
    }

    @Transient
    @JsonView(Views.Public.class)
    public Set<StaticFile> getPublicFiles() {
        return files == null ? null : files.stream().filter(it -> it.getRole()==null).collect(Collectors.toSet());
    }
}
