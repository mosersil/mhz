package com.silviomoser.mhz.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileNameUtils {

    public static String normalizeFileName(String input) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        final StringBuilder fileName = new StringBuilder()
                .append(dateFormat.format(Calendar.getInstance().getTime()))
                .append("-")
                .append(input.replaceAll(" ", "_").toLowerCase());
        return fileName.toString();
    }
}
