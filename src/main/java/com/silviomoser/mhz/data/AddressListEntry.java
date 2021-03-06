package com.silviomoser.mhz.data;

import com.silviomoser.mhz.data.type.Gender;
import com.silviomoser.mhz.data.type.PreferredChannel;
import com.silviomoser.mhz.utils.PdfReport;
import com.silviomoser.mhz.utils.XlsReport;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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
    @XlsReport(header = "Firma")
    @Column(name = "COMPANY")
    private String company;
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
    @PdfReport(header = "Mobiltelefon")
    @XlsReport(header = "Mobiltelefon")
    @Column(name = "MOBILE")
    private String mobile;
    @XlsReport(header = "Telefon")
    @Column(name = "LANDLINE")
    private String landline;
    @PdfReport(header = "Email")
    @XlsReport(header = "Email")
    @Column(name = "EMAIL")
    private String email;
    @XlsReport(header = "Geburtstag")
    @Column(name = "BIRTHDATE")
    private String birthDate;
    @XlsReport(header = "Kommunikation")
    @Column(name = "PREF_CHANNEL")
    private PreferredChannel channel;
    @Column(name = "ORGANIZATION")
    private String organization;


}
