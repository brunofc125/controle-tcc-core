package com.controletcc.util;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtil {

    private LocalDateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Compares two LocalDate values.
     *
     * @param date1 First LocalDate value
     * @param date2 Second LocalDate value
     * @return 0 if equals, -1 if date1 is before the date2 and 1 if date1 is after the date2
     */
    public static int compare(@NonNull LocalDate date1, @NonNull LocalDate date2) {
        if (date1.equals(date2)) {
            return 0;
        } else if (date1.isBefore(date2)) {
            return -1;
        }
        return 1;
    }

    public static String localDateToString(@NonNull LocalDate date, @NonNull String pattern) {
        if (pattern.isBlank()) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate stringToLocalDate(@NonNull String dateString, @NonNull String pattern) {
        if (dateString.isBlank() || pattern.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }

}
