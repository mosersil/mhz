package com.silviomoser.demo.data;


import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.FileType;
import com.silviomoser.demo.data.type.StaticFileCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity(name = "STATIC_FILE")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StaticFile extends AbstractEntity {

    @JsonView(Views.Public.class)
    @Column(name = "TITLE")
    @Size(max = 50)
    private String title;

    @JsonView(Views.Public.class)
    @Column(name = "CATEGORY")
    private StaticFileCategory staticFileCategory;

    @JsonView(Views.Public.class)
    @Column(name = "DESCRIPTION")
    @Size(max = 600)
    private String description;

    @JsonView(Views.Public.class)
    @Column(name = "TYPE")
    private FileType fileType;

    @JsonView(Views.Public.class)
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    @ToString.Exclude
    private Person person;

    @Column(name = "LOCATION")
    @Size(max = 100)
    private String location;

    @JsonView(Views.Public.class)
    @Column(name = "KEYWORDS")
    @Size(max = 200)
    private String keywords;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private CalendarEvent event;

    @Transient
    @JsonView(Views.Public.class)
    public String getMimeType() {
        return fileType.getMime();
    }

    @Transient
    @JsonView(Views.Public.class)
    public String getEnding() {
        return fileType.getEnding();
    }

    @Transient
    @JsonView(Views.Public.class)
    public String getIcon() {
        return fileType.getIcon();
    }
}
