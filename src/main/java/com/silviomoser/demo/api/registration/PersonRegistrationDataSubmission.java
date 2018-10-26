package com.silviomoser.demo.api.registration;


import lombok.Data;

@Data
public class PersonRegistrationDataSubmission {

    private String gender;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String address2;
    private String zip;
    private String city;
    private String password;
    private String password_confirmation;
}
