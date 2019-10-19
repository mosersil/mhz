package com.silviomoser.mhz.view;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class PdfViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        if (s.equalsIgnoreCase("PDF")) {
            return new PdfView();
        } else {
            return null;
        }
    }
}
