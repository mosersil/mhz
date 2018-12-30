package com.silviomoser.demo.api.contact;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Created by silvio on 20.08.18.
 */
@Data
public class ContactFormModel {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String message;

    @NotBlank
    private String captchaCode;

    @NotBlank
    private String captchaId;

}