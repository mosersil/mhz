package com.silviomoser.demo.security;

import lombok.Data;

@Data
public class AuthUser {

    private String firstName;
    private String lastName;
    private AuthRole[] authRoles;

}
