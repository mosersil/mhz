package com.silviomoser.demo.utils;

import com.silviomoser.demo.data.Person;

import static com.silviomoser.demo.utils.StringUtils.isNotBlank;

/**
 * Created by silvio on 13.10.18.
 */
public class FormatUtils {

    private static final String LINE_BREAK = "\n";

    public static final String toFirstLastName(Person person) {
        if (person == null) {
            return "";
        }
        return person.getFirstName() + " " + person.getLastName();
    }

    public static final String welcomingInformal(Person person) {
        if (person == null) {
            return "";
        }

        StringBuilder welcoming = new StringBuilder("Hallo");

        if (isNotBlank(person.getFirstName())) {
            welcoming.append(" " + person.getFirstName());
        }

        return welcoming.toString();
    }


    public static final String fullAddress(Person person) {
        if (person == null) {
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder(toFirstLastName(person))
                .append(LINE_BREAK)
                .append(person.getAddress1());
        if (person.getAddress2() != null && !person.getAddress2().isEmpty()) {
            stringBuilder
                    .append(LINE_BREAK)
                    .append(person.getAddress2());
        }
        stringBuilder.append(LINE_BREAK)
                .append(person.getZip())
                .append(" ")
                .append(person.getCity());
        return stringBuilder.toString();
    }

}
