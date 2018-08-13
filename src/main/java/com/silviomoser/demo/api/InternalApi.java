package com.silviomoser.demo.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.utils.PdfBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

/**
 * Created by silvio on 29.07.18.
 */
@RestController
public class InternalApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CalendarEventRepository calendarEventRepository;

    @JsonView(Views.Public.class)
    @RequestMapping("/internal/api/user")
    public Person my() {
        return SecurityUtils.getMy();
    }


    @RequestMapping(value="/internal/api/calendar", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPDF() throws DocumentException {



        ByteArrayInputStream bis = PdfBuilder.generatePdfListReport(calendarEventRepository.findAll(), CalendarEvent.class);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=jahresprogramm.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }
}
