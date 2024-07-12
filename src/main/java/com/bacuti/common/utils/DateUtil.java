package com.bacuti.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

/**
 * Common util methods for date conversions.
 */
public class DateUtil {

    /**
     * Converts Date as String of format (dd/MMM/yyyy) to Local date.
     *
     * @param dateAsString to be parsed.
     * @return converted string as local date.
     */
    public static LocalDate convertToLocalDateFromString(String dateAsString) {
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
            .appendPattern("dd/MMM/yyyy").toFormatter(Locale.ENGLISH);
        try {
            return LocalDate.parse(dateAsString, inputFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
