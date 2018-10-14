package com.silviomoser.demo.utils;

import com.silviomoser.demo.data.Person;

/**
 * Created by silvio on 13.10.18.
 */
public class FormatUtils {

    public static final String toFirstLastName(Person person) {
        if (person==null) {
            return "";
        }
        return person.getFirstName()+ " " + person.getLastName();
    }
}
