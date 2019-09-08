package com.silviomoser.mhz.security;

import lombok.Data;

@Data
public class AuthUser {

    private String firstName;
    private String lastName;
    private AuthRole[] authRoles;

}
