package com.silviomoser.demo.api.core;

public interface ApiController {

    String URL_CALENDAR = "/public/api/calendar";
    String URL_DOWNLOADS = "/public/api/download";
    String URL_AUTH_USER = "/auth/user";
    String URL_AUTH_INITPWRESET = "/auth/initPwReset";
    String URL_AUTH_REDEEMTOKEN = "/auth/redeemToken";

    String URL_INTERNAL_ADDRESSLIST = "/api/protected/internal/addresslist";
    String URL_INTERNAL_CALENDAR = "/api/protected/internal/calendar";

    String URL_PUBLIC_IMAGES = "/api/public/images";
    String URL_PUBLIC_IMAGE = "/api/public/image";
}
