package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.Gender;
import com.silviomoser.demo.utils.OrganizationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by silvio on 26.05.18.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "PERSON")
public class Person extends AbstractEntity {


    @JsonView(Views.Public.class)
    @NotNull
    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[A-Z][a-z]+")
    private String firstName;

    @JsonView(Views.Public.class)
    @NotNull
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[A-Za-z\\s]+")
    private String lastName;

    @JsonView(Views.Public.class)
    @NotNull
    @Column(name = "GENDER", nullable = false)
    private Gender gender;

    @JsonView(Views.Public.class)
    @NotNull
    @Pattern(regexp = "[A-Za-z\\s]+")
    @Column(name = "ADDRESS1", nullable = false, length = 50)
    private String address1;

    @JsonView(Views.Public.class)
    //@Pattern(regexp = "[a-z]+")
    @Column(name = "ADDRESS2", length = 50)
    private String address2;

    @JsonView(Views.Public.class)
    //@Pattern(regexp = "[0-9]+")
    @Column(name = "ZIP", nullable = false, length = 10)
    private String zip;

    @JsonView(Views.Public.class)
    //@Pattern(regexp = "[A-Z][a-z\\s]+")
    @Column(name = "CITY", nullable = false, length = 30)
    private String city;

    @Column(name = "LANDLINE", length = 20)
    //@Pattern(regexp = "[0-9]+")
    private String landline;

    @Column(name = "MOBILE", length = 20)
    //@Pattern(regexp = "[0-9]+")
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


    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PersonVerification personVerification;

    @JsonView(Views.Public.class)
    @Transient
    public List<String> getOrganizations() {
        return OrganizationUtils.getActiveOrganizations(this);
    }

}
