package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.Gender;
import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.utils.OrganizationUtils;
import com.silviomoser.demo.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

import static com.silviomoser.demo.utils.StringUtils.capitalizeFirstCharacter;
import static com.silviomoser.demo.utils.StringUtils.isNotBlank;

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
    @Pattern(regexp = "^[\\p{L}][-\\s\\p{L}]+[\\p{L}]")
    private String firstName;

    @JsonView(Views.Public.class)
    @NotNull
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[\\p{L}][-\\s\\p{L}]+[\\p{L}]")
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


    public static final PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static class PersonBuilder {


        private String firstName;
        private String lastName;
        private String gender;
        private String address1;
        private String address2;
        private String zip;
        private String city;
        private String landline;
        private String mobile;
        private String email;
        private String password;
        private String[] roles;
        private String verificationToken;

        public PersonBuilder firstName(String firstName) {
            if (isNotBlank(firstName)) {
                this.firstName = capitalizeFirstCharacter(firstName);
            }
            return this;
        }

        public PersonBuilder lastName(String lastName) {
            if (isNotBlank(lastName)) {
                this.lastName = lastName;
            }
            return this;
        }

        public PersonBuilder gender(String gender) {
            if (isNotBlank(gender)) {
                this.gender = gender;
            }
            return this;
        }

        public PersonBuilder address1(String address1) {
            if (isNotBlank(address1)) {
                this.address1 = address1;
            }
            return this;
        }

        public PersonBuilder address2(String address2) {
            if (isNotBlank(address2)) {
                this.address2 = address2;
            }
            return this;
        }

        public PersonBuilder zip(String zip) {
            if (isNotBlank(zip)) {
                this.zip = zip;
            }
            return this;
        }

        public PersonBuilder city(String city) {
            if (isNotBlank(city)) {
                this.city = city;
            }
            return this;
        }

        public PersonBuilder landline(String landline) {
            if (isNotBlank(landline)) {
                this.landline = landline;
            }
            return this;
        }

        public PersonBuilder mobile(String mobile) {
            if (isNotBlank(mobile)) {
                this.mobile = mobile;
            }
            return this;
        }

        public PersonBuilder email(String email) {
            if (isNotBlank(email)) {
                this.email = email;
            }
            return this;
        }

        public PersonBuilder password(String password) {
            if (isNotBlank(password)) {
                this.password = password;
            }
            return this;
        }

        public PersonBuilder verificationToken(String verificationToken) {
            if (isNotBlank(verificationToken)) {
                this.verificationToken = verificationToken;
            }
            return this;
        }

        public PersonBuilder roles(String... roles) {
            this.roles = roles;
            return this;
        }

        public PersonBuilder roles(String roles) {
            if (isNotBlank(roles)) {
                String[] splitted = roles.split(",");
                this.roles = StringUtils.stripAll(splitted);
            }

            return this;
        }


        public Person bulid() {
            Person person = new Person();
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setAddress1(address1);
            person.setAddress2(address2);
            person.setZip(zip);
            person.setCity(city);
            person.setLandline(landline);
            person.setMobile(mobile);
            person.setEmail(email);

            if (isNotBlank(gender)) {
                person.setGender(Gender.valueOf(gender.toUpperCase()));
            }

            if (isNotBlank(password) && isNotBlank(email)) {
                final User user = new User();
                user.setCreatedDate(new Date());
                user.setPerson(person);
                user.setUsername(email);
                List<Role> roleList = new ArrayList<>();
                if (roles != null && roles.length > 0) {
                    Arrays.stream(roles).forEach(it -> {
                        try {
                            Role role = new Role();
                            role.setType(RoleType.valueOf(it.trim().toUpperCase()));
                            roleList.add(role);
                        } catch (IllegalArgumentException iae) {
                            //LOGGER.error("Illegal value for RoleType: " + iae.getMessage(), iae);
                        }
                    });
                }
                if (roleList.size() > 0) {
                    user.setRoles(roleList);
                }

                final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                final String hashedPassword = passwordEncoder.encode(password);

                user.setPassword(hashedPassword);
                person.setUser(user);


                if (isNotBlank(verificationToken)) {
                    PersonVerification personVerification = new PersonVerification(person, verificationToken, LocalDateTime.now(), null);
                }
            }
            return person;
        }
    }

}
