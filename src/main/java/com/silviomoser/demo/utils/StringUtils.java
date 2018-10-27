package com.silviomoser.demo.utils;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Arrays;

public class StringUtils {

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static String[] stripAll(String[] input) {
        if (input!=null) {
            final String[] stripped = new String[input.length];
            Arrays.stream(input).map(String::trim).toArray(it -> stripped);
            return stripped;
        }
        return null;

    }


    public static boolean isValidEmailAddress(String input) {
        return  (isNotBlank(input) && EmailValidator.getInstance().isValid(input));
    }
}
