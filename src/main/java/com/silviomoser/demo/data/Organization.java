package com.silviomoser.demo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by silvio on 27.05.18.
 */
@Entity
@Table(name = "ORGANIZATION")
public class Organization {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;
    @Column(name = "NAME")
    private String name;

    /*
    @ManyToMany(mappedBy = "organizations")
    private Set<Person> people;
    */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /*
    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }
    */

}
