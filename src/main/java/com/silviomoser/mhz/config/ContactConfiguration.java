package com.silviomoser.mhz.config;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by silvio on 26.08.18.
 */
@Data
public class ContactConfiguration {

    @NotBlank
    private String contactEmailFrom;
    @NotBlank
    private String contactEmailTo;
    @NotBlank
    private String getContactEmailSubject;

}
