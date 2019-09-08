package com.silviomoser.mhz.utils;

import com.silviomoser.mhz.data.Person;

public class PersonUtils {


    public static boolean isDeletable(Person person) {
        if (person!=null && person.getMemberships()!=null  && person.getMemberships().size()>0) {
            return false;
        }
        return true;
    }
}
