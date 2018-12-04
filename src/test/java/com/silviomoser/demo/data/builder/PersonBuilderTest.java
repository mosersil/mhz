package com.silviomoser.demo.data.builder;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.utils.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

public class PersonBuilderTest {

    @DataProvider(name = "testBulidDp")
    public Object[][] testBulidDp() {
        return new Object[][]{
                {"male", "Max", "Muster", "Strasse 11", "Postfach", "8000", "Zürich", "test@test.com", "mypassword11", null, "00123456", "00123456"},
                {"male", "Max", "Muster", "Strasse 11", null, "8000", "Zürich", "test@test.com", "mypassword11", null, "00123456", "00123456"},
                {null, "Max", "Muster", "Strasse 11", null, "8000", "Zürich", "test@test.com", "mypassword11", null, "00123456", "00123456"},
                {"male", "Max", "Muster", "Strasse 11", null, "8000", "Zürich", "test@test.com", "mypassword11", "user,admin", "00123456", "00123456"}

        };
    }

    @Test(dataProvider = "testBulidDp")
    public void testBulid(String gender, String firstName, String lastName, String address1, String address2, String zip,
                          String city, String email, String password, String roles, String landline, String mobile) {
        Person person = Person.builder()
                .gender(gender)
                .firstName(firstName)
                .lastName(lastName)
                .address1(address1)
                .address2(address2)
                .zip(zip)
                .city(city)
                .email(email)
                .password(password)
                .roles(roles)
                .landline(landline)
                .mobile(mobile)
                .bulid();

        if (gender == null || gender.trim().isEmpty()) {
            assertThat(person.getGender()).isNull();
        } else {
            assertThat(person.getGender().name()).isEqualToIgnoringCase(gender);
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            assertThat(person.getFirstName()).isNull();
        } else {
            assertThat(person.getFirstName()).isEqualTo(firstName);
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            assertThat(person.getLastName()).isNull();
        } else {
            assertThat(person.getLastName()).isEqualTo(lastName);
        }

        if (address1 == null || address1.trim().isEmpty()) {
            assertThat(person.getAddress1()).isNull();
        } else {
            assertThat(person.getAddress1()).isEqualTo(address1);
        }

        if (address2 == null || address2.trim().isEmpty()) {
            assertThat(person.getAddress2()).isNull();
        } else {
            assertThat(person.getAddress2()).isEqualTo(address2);
        }

        if (zip == null || zip.trim().isEmpty()) {
            assertThat(person.getZip()).isNull();
        } else {
            assertThat(person.getZip()).isEqualTo(zip);
        }

        if (city == null || city.trim().isEmpty()) {
            assertThat(person.getCity()).isNull();
        } else {
            assertThat(person.getCity()).isEqualTo(city);
        }

        if (landline == null || landline.trim().isEmpty()) {
            assertThat(person.getLandline()).isNull();
        } else {
            assertThat(person.getLandline()).isEqualTo(landline);
        }

        if (mobile == null || mobile.trim().isEmpty()) {
            assertThat(person.getMobile()).isNull();
        } else {
            assertThat(person.getMobile()).isEqualTo(mobile);
        }

        if (email == null || email.trim().isEmpty()) {
            assertThat(person.getEmail()).isNull();
        } else {
            assertThat(person.getEmail()).isEqualTo(email);
        }

        if (email != null && password != null) {
            assertThat(person.getUser().getPassword()).hasSize(60);
        } else {
            assertThat(person.getUser()).isNull();
        }
        if (roles != null) {

            List<Role> roleList = buildRolesList(roles);
            assertThat(person.getUser().getRoles()).containsAll(roleList);
        }
    }

    private List<Role> buildRolesList(String roles) {
        String[] splitted = roles.split(",");
        return Arrays.stream(splitted).map(it -> {
            Role role = new Role();
            role.setType(RoleType.valueOf(it.trim().toUpperCase()));
            return role;
        }).collect(Collectors.toList());
    }

}