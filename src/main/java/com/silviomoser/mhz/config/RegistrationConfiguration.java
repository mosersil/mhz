package com.silviomoser.mhz.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class RegistrationConfiguration {

    @NotBlank
    private String emailFrom;
    @NotBlank
    private String baseUrl;

}
