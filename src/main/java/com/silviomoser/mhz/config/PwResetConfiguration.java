package com.silviomoser.mhz.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class PwResetConfiguration {

    @NotBlank
    private String emailFrom;
    @NotBlank
    private String landingPage;
    @NotBlank
    private String baseUrl;

}
