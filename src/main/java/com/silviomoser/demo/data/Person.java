package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.Gender;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by silvio on 26.05.18.
 */
@Entity
@Table(name = "PERSON")
public class Person extends AbstractEntity {


    @JsonView(Views.Public.class)
    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    private String firstName;
    @JsonView(Views.Public.class)
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    private String lastName;
    @JsonView(Views.Public.class)
    @Column(name = "GENDER")
    private Gender gender;
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
    @Column(name = "LANDLINE", nullable = false, length = 20)
    private String landline;
    @Column(name = "MOBILE", nullable = false, length = 20)
    private String mobile;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "person"
    )
    private Set<Membership> memberships;

    @JsonView(Views.Public.class)
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
