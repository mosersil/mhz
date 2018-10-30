package com.silviomoser.demo.data;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private String title;

    @NotNull
    @Column(name = "TEXT")
    private String text;

    @NotNull
    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "END_DATE")
    private LocalDateTime endDate;


}
