package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.Gender;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by silvio on 26.05.18.
 */
@Entity
@Table(name = "PERSON")
public class Person {



    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;
    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    private String lastName;
    @Column(name = "GENDER")
    private Gender gender;
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "person"
    )
    private Set<Membership> memberships;

    /*

    @JoinTable(name = "PERSON_ORGANIZATION", joinColumns = {
            @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Organization> organizations;

    */


    public long getId() {
        return id;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    public void setId(long id) {
        this.id = id;
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

    /*

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    */
}
