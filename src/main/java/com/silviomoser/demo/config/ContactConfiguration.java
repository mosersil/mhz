package com.silviomoser.demo.config;

import javax.validation.constraints.NotBlank;

/**
 * Created by silvio on 26.08.18.
 */
public class ContactConfiguration {

    @NotBlank
    private String contactEmailTo;
    @NotBlank
    private String getContactEmailSubject;

    public String getContactEmailTo() {
        return contactEmailTo;
    }

    public void setContactEmailTo(String contactEmailTo) {
        this.contactEmailTo = contactEmailTo;
    }

    public String getGetContactEmailSubject() {
        return getContactEmailSubject;
    }

    public void setGetContactEmailSubject(String getContactEmailSubject) {
        this.getContactEmailSubject = getContactEmailSubject;
    }
}
