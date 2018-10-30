package com.silviomoser.demo.api.registration;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PersonRegistrationDataSubmission {

    private String gender;
    private String firstName;
    private String lastName;
    private String email;
    private String address1;
    private String address2;
    private String zip;
    private String city;
    private String password;
    private String password_confirmation;
}
