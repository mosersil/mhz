package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.Gender;
import com.silviomoser.demo.utils.PdfReport;
import com.silviomoser.demo.utils.XlsReport;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by silvio on 15.08.18.
 */
@Entity
@Immutable
@Table(name = "VIEW_ADDRESSLIST")
public class AddressListEntry  extends AbstractEntity {

    @PdfReport(header = "Anrede")
    @XlsReport(header = "Anrede")
    @Column(name = "GENDER")
    private Gender gender;
    @PdfReport(header = "Vorname")
    @XlsReport(header = "Vorname")
    @Column(name = "FIRST_NAME")
    private String firstName;
    @XlsReport(header = "Name")
    @PdfReport(header = "Name")
    @Column(name = "LAST_NAME")
    private String lastName;
    @PdfReport(header = "Adresse")
    @XlsReport(header = "Adresse")
    @Column(name = "ADDRESS")
    private String address;
    @PdfReport(header = "PLZ")
    @XlsReport(header = "PLZ")
    @Column(name = "ZIP")
    private String zip;
    @PdfReport(header = "Ort")
    @XlsReport(header = "Ort")
    @Column(name = "CITY")
    private String city;
    @Column(name = "ORGANIZATION")
    private String organization;



    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress1(String address1) {
        this.address = address1;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
