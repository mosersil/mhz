package com.silviomoser.demo.api.internal;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class PostContactForm {

    @Pattern(regexp = "[0-9\\s]+")
    private String mobile;

    @Pattern(regexp = "[0-9\\s]+")
    private String landline;

    @Email
    private String email;
}
