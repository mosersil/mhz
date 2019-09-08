package com.silviomoser.mhz.api.authentication;

import lombok.Data;

@Data
public class ResetPasswordForm {

    String email;
    String username;
    String forward;

}
