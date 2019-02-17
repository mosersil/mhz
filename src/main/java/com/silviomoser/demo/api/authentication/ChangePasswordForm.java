package com.silviomoser.demo.api.authentication;

import lombok.Data;

@Data
public class ChangePasswordForm {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
