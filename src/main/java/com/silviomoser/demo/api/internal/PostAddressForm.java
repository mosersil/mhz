package com.silviomoser.demo.api.internal;

import com.silviomoser.demo.data.type.Gender;
import lombok.Data;

@Data
public class PostAddressForm {
    private Gender gender;
    private String title;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String zip;
    private String city;
}
