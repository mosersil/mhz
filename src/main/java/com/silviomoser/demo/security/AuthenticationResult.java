package com.silviomoser.demo.security;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by silvio on 31.07.18.
 */
@Data
@Builder
@ToString
public class AuthenticationResult {
    private String jwt;
    private AuthUser authUser;
}
