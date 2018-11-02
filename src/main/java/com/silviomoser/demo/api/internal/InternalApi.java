package com.silviomoser.demo.api.internal;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;
import com.silviomoser.demo.data.AddressListEntry;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.repository.AddressListRepository;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.utils.PdfBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * Created by silvio on 29.07.18.
 */
@RestController
public class InternalApi {

    @Autowired
    CalendarEventRepository calendarEventRepository;

    @Autowired
    AddressListRepository addressListRepository;

    @JsonView(Views.Public.class)
    @RequestMapping("/internal/api/user")
    public Person my() {
        return SecurityUtils.getMe();
    }


    @RequestMapping(value="/internal/api/calendar", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDF(@RequestParam(name = "year", required = false) String  year) throws DocumentException {
        final LocalDate now = LocalDate.now();
        final LocalDate from = LocalDate.of(Integer.parseInt(year), 1, 1);
        final LocalDate to = from.with(lastDayOfYear());;
        final ByteArrayInputStream bis = PdfBuilder.generatePdfListReport(calendarEventRepository.findCalendarEventsBetween(from.atStartOfDay(), to.atTime(23, 59)), CalendarEvent.class);
        return pdfResponse(bis, "jahresprogramm"+year);
    }


    @RequestMapping(value="/internal/api/addresslist", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getAddressList(@RequestParam(name = "organization", required = true) String  organization) throws DocumentException {
        final ByteArrayInputStream bis = PdfBuilder.generatePdfListReport(addressListRepository.findByOrganization(organization), AddressListEntry.class);
        return pdfResponse(bis, organization);
    }



    private ResponseEntity<InputStreamResource> pdfResponse(ByteArrayInputStream bis, String fileName) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("inline; filename=%s.pdf", fileName));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
