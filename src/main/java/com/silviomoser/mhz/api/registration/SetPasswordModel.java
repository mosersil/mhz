package com.silviomoser.mhz.api.registration;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SetPasswordModel {
    @NotNull
    @Size(max = 50)
    private String token;
    @NotNull
    @Size(max = 30)
    private String password;
    @NotNull
    @Size(max = 30)
    private String passwordConfirmation;
}
