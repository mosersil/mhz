package com.silviomoser.demo.data.builder;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.PersonVerification;
import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.data.type.Gender;
import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.silviomoser.demo.utils.StringUtils.isNotBlank;

public class PersonBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonBuilder.class);

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
            this.firstName = firstName;
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

    public PersonBuilder roles(String... roles ) {
        this.roles=roles;
        return this;
    }

    public PersonBuilder roles(String roles) {
        if (isNotBlank(roles)) {
            String[] splitted = roles.split(",");
            this.roles=StringUtils.stripAll(splitted);
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
            if (roles!=null && roles.length>0) {
                Arrays.stream(roles).forEach(it -> {
                            try {
                                Role role = new Role();
                                role.setType(RoleType.valueOf(it.trim().toUpperCase()));
                                roleList.add(role);
                            } catch (IllegalArgumentException iae) {
                                LOGGER.error("Illegal value for RoleType: " + iae.getMessage(), iae);
                            }
                } );
            }
            if (roleList.size()>0) {
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
