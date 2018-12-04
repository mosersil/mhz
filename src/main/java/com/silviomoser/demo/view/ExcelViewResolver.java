package com.silviomoser.demo.view;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class ExcelViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        if (s.equalsIgnoreCase("XLS")) {
            return new ExcelView();
        } else {
            return null;
        }
    }

}