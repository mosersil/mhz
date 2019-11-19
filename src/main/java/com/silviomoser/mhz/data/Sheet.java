package com.silviomoser.mhz.data;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "SHEET")
public class Sheet extends AbstractEntity {

    @JsonView(Views.Public.class)
    @Column(name = "TITLE")
    String title;

    @JsonView(Views.Public.class)
    @Column(name = "LOCATION")
    String location;

}
