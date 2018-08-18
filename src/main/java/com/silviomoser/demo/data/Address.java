package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by silvio on 13.08.18.
 */
@Entity
@Table(name = "ADDRESS")
public class Address extends AbstractEntity {

    @JsonView(Views.Public.class)
    @Column(name = "ADDRESS1", nullable = false, length = 50)
    private String address1;
    @JsonView(Views.Public.class)
    @Column(name = "ADDRESS2", length = 50)
    private String address2;
    @JsonView(Views.Public.class)
    @Column(name = "ZIP", nullable = false, length = 10)
    private String zip;
    @JsonView(Views.Public.class)
    @Column(name = "CITY", nullable = false, length = 30)
    private String city;

    @OneToMany(mappedBy = "address")
    private List<Person> people;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }
}
