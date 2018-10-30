package com.silviomoser.demo.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by silvio on 27.05.18.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "ORGANIZATION")
public class Organization extends AbstractEntity {

    @Column(name = "NAME")
    private String name;


}
