package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by silvio on 23.07.18.
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ARTICLE")
public class Article extends AbstractEntity {


    @NotNull
    @Column(name = "TITLE")
    @JsonView(Views.Public.class)
    private String title;

    @NotNull
    @Column(name = "TEASER")
    @JsonView(Views.Public.class)
    private String teaser;

    @NotNull
    @Column(name = "TEXT")
    @JsonView(Views.Public.class)
    private String text;

    @NotNull
    @Column(name = "START_DATE")
    @JsonView(Views.Public.class)
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "END_DATE")
    @JsonView(Views.Public.class)
    private LocalDateTime endDate;

    @JsonView(Views.Public.class)
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<CalendarEvent> events;

}
