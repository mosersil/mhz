package com.silviomoser.mhz.api.registration;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class PersonRegistrationModel {

    @NotNull
    private String gender;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String address1;
    @NotNull
    private String address2;
    @NotNull
    private String zip;
    @NotNull
    private String city;
    @NotNull
    private String preferredChannel;
    @NotNull
    private String captchaId;
    @NotNull
    private String captchaCode;
    private String password;
    private String password_confirmation;
}
