package com.controletcc.util;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    private LocalDateTimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Compares two LocalDate values.
     *
     * @param date1 First LocalDate value
     * @param date2 Second LocalDate value
     * @return 0 if equals, -1 if date1 is before the date2 and 1 if date1 is after the date2
     */
    public static int compare(@NonNull LocalDateTime date1, @NonNull LocalDateTime date2) {
        if (date1.equals(date2)) {
            return 0;
        } else if (date1.isBefore(date2)) {
            return -1;
        }
        return 1;
    }

    public static String localDateTimeToString(@NonNull LocalDateTime date, @NonNull String pattern) {
        if (pattern.isBlank()) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime stringToLocalDateTime(@NonNull String dateString, @NonNull String pattern) {
        if (dateString.isBlank() || pattern.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }

}
