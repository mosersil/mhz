package com.silviomoser.mhz.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.data.type.Gender;
import com.silviomoser.mhz.data.type.PreferredChannel;
import com.silviomoser.mhz.utils.OrganizationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

import static com.silviomoser.mhz.utils.StringUtils.capitalizeFirstCharacter;
import static com.silviomoser.mhz.utils.StringUtils.isNotBlank;

/**
 * Created by silvio on 26.05.18.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "PERSON")
public class Person extends AbstractEntity {

    private static final String MSG_VALIDCHARACTERS = "Bitte nur gültige Zeichen verwenden oder Feld komplett leer lassen";
    private static final String REGEXP_VALID_NAME = "^[\\p{L}][-\\&\\s\\p{L}]+[\\p{L}]";
    private static final String REGEXP_VALID_INT_PHONE = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$";

    @JsonView(Views.Internal.class)
    @NotNull
    @Column(name = "GENDER", nullable = false)
    private Gender gender;

    @JsonView({Views.Public.class, Views.Internal.class})
    @Column(name = "TITLE", length = 30)
    @Size(max = 30)
    private String title;

    @JsonView({Views.Public.class, Views.Internal.class})
    @NotNull
    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    @Size(min = 2, max = 30)
    @Pattern(regexp = REGEXP_VALID_NAME, message = MSG_VALIDCHARACTERS)
    private String firstName;

    @JsonView({Views.Public.class, Views.Internal.class})
    @NotNull
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    @Size(min = 2, max = 30)
    @Pattern(regexp = REGEXP_VALID_NAME, message = MSG_VALIDCHARACTERS)
    private String lastName;

    @JsonView({Views.Public.class, Views.Internal.class})
    @Column(name = "COMPANY", length = 30)
    @Size(max = 30)
    @Pattern(regexp = REGEXP_VALID_NAME, message = MSG_VALIDCHARACTERS)
    private String company;

    @JsonView(Views.Internal.class)
    @NotNull
    @Column(name = "ADDRESS1", nullable = false, length = 50)
    private String address1;

    @JsonView(Views.Internal.class)
    @Column(name = "ADDRESS2", length = 50)
    private String address2;

    @NotNull
    @JsonView(Views.Internal.class)
    @Pattern(regexp = "[0-9]+")
    @Column(name = "ZIP", nullable = false, length = 10)
    private String zip;

    @NotNull
    @JsonView({Views.Internal.class})
    @Pattern(regexp = "^[\\p{L}][-\\s\\p{L}]+[\\p{L}]")
    @Column(name = "CITY", nullable = false, length = 30)
    private String city;

    @JsonView({Views.Internal.class})
    @Column(name = "LANDLINE", length = 20)
    @Pattern(regexp = REGEXP_VALID_INT_PHONE, message = "Internationales Format, z.B. +4179xxxxxx")
    private String landline;

    @JsonView({Views.Internal.class})
    @Column(name = "MOBILE", length = 20)
    @Pattern(regexp = REGEXP_VALID_INT_PHONE, message = "Internationales Format, z.B. +4179xxxxxx")
    private String mobile;

    @Email
    @JsonView({Views.Internal.class})
    @Column(name = "EMAIL", length = 50, unique = true)
    private String email;

    @Column(name = "BIRTHDATE")
    @JsonView({Views.Internal.class})
    private LocalDate birthDate;

    @Column(name = "REMARKS", length = 500)
    private String remarks;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "person"
    )
    private Set<Membership> memberships;

    @JsonView(Views.Internal.class)
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;


    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PersonVerification personVerification;

    @NotNull
    @Column(name = "PREF_CHANNEL", nullable = false)
    private PreferredChannel preferredChannel;

    @JsonView({Views.Public.class, Views.Internal.class})
    @Transient
    public Set<String> getOrganizations() {
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
        private String preferedChannel;

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

        public PersonBuilder preferedChannel(String preferedChannel) {
            if (isNotBlank(preferedChannel)) {
                this.preferedChannel = preferedChannel;
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

            if (isNotBlank(preferedChannel)) {
                person.setPreferredChannel(PreferredChannel.valueOf(preferedChannel.toUpperCase()));
            }

            return person;
        }
    }

}
