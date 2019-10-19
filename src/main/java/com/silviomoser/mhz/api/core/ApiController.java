package com.silviomoser.mhz.api.core;

public interface ApiController {

    String URL_CALENDAR = "/public/api/calendar";
    String URL_DOWNLOADS = "/public/api/download";
    String URL_MEMBERLIST = "/api/public/members";
    String URL_MEMBERMETA = "/api/public/member_data";
    String URL_AUTH_USER = "/auth/user";
    String URL_AUTH_INITPWRESET = "/auth/initPwReset";
    String URL_AUTH_REDEEMTOKEN = "/auth/redeemToken";
    String URL_AUTH_CHANGEPW = "/api/protected/auth/changepassword";

    String URL_INTERNAL_ADDRESSLIST = "/api/protected/internal/addresslist";
    String URL_INTERNAL_DOWNLOAD = "/api/protected/internal/download";
    String URL_INTERNAL_CALENDAR = "/api/protected/internal/calendar";
    String URL_INTERNAL_FILES = "/api/protected/internal/staticfiles";
    String URL_INTERNAL_ADDRESS = "/api/protected/internal/address";
    String URL_INTERNAL_CONTACTDETAILS = "/api/protected/internal/contactdetails";
    String URL_INTERNAL_BIRTHDAY = "/api/protected/internal/birthday";

    String URL_PUBLIC_IMAGES = "/api/public/images";
    String URL_PUBLIC_IMAGE = "/api/public/image";

    String URL_CONTACT = "/api/public/contact";
}
