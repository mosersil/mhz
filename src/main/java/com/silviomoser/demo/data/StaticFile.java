package com.silviomoser.demo.data;


import com.silviomoser.demo.data.type.FileType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity(name = "STATIC_FILE")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StaticFile extends AbstractEntity {

    @Column(name = "TITLE")
    @Size(max = 50)
    private String title;

    @Column(name = "DESCRIPTION")
    @Size(max = 600)
    private String description;

    @Column(name = "TYPE")
    private FileType fileType;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @Column(name = "LOCATION")
    @Size(max = 100)
    private String location;

    @Column(name = "KEYWORDS")
    @Size(max = 200)
    private String keywords;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private CalendarEvent event;
}
