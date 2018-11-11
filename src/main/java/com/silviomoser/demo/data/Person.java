package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by silvio on 26.05.18.
 */
@Entity
@Getter
@Setter
@Table(name = "PERSON")
public class Person extends AbstractEntity {


    @JsonView(Views.Public.class)
    @NotNull
    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    private String firstName;

    @JsonView(Views.Public.class)
    @NotNull
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    private String lastName;

    @JsonView(Views.Public.class)
    @NotNull
    @Column(name = "GENDER", nullable = false)
    private Gender gender;

    @JsonView(Views.Public.class)
    @NotNull
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

    @Column(name = "LANDLINE", length = 20)
    private String landline;

    @Column(name = "MOBILE", length = 20)
    private String mobile;

    @Email
    @Column(name = "Email", length = 50, unique = true)
    private String email;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "person"
    )
    private Set<Membership> memberships;

    @JsonView(Views.Public.class)
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", landline='" + landline + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", user=" + user +
                '}';
    }
}
