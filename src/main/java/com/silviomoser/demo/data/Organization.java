package com.silviomoser.demo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by silvio on 27.05.18.
 */
@Entity
@Table(name = "ORGANIZATION")
public class Organization extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
