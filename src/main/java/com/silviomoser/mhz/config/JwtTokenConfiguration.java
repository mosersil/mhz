package com.silviomoser.mhz.config;


import lombok.Data;

@Data
public class JwtTokenConfiguration {

    private String jwtSecret;
    private int jwtExpirationSecs = 86400;

    public int getExpirationInMillis() {
        return jwtExpirationSecs*1000;
    }

}
