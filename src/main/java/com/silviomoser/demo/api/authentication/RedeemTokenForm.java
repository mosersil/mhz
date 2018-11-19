package com.silviomoser.demo.api.authentication;

import lombok.Data;

@Data
public class RedeemTokenForm {
    private String password;
    private String password_confirmation;
    private String token;
    private String forward;

}
