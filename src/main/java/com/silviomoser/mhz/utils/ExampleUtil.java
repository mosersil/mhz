package com.silviomoser.mhz.utils;

import com.silviomoser.mhz.security.utils.SecurityUtils;

/**
 * Created by silvio on 10.05.18.
 */
public class ExampleUtil {

    public static String lorem() {
        return "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    }

    public static String home() {
        return "<h1>Willkommen, "+ SecurityUtils.getLoggedInUserFullName()+"!</h1><p>In dieser Demo funktioniert nur der Kalender (und auch dieser nur Ansatzweise...). Alle anderen Links führen ins Nirgendwo.</p>" +
                "<p>Und nun das coole am ganzen: Wenn du den Kalender angepasst hast, werden deine Daten in einer Datenbank gespeichert. Der Kalender steht dann als sogenanntes API zur verfügung." +
                "<a href=\"/public/api/calendar\">Und zwar hier</a></p>" +
                "<p>Zum öffentlichen Bereich <a href=\"/\">geht es hier lang</a></p>"
                ;
    }
}
