package com.controletcc.util;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

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

    public static LocalDate stringToLocalDateYY(@NonNull String dateString) {
        if (dateString.isBlank()) {
            return null;
        }
        DateTimeFormatter formatter = getDateTimeFormatterYY("dd/MM/");
        return LocalDate.parse(dateString, formatter);
    }

    private static DateTimeFormatter getDateTimeFormatterYY(@NonNull String pattern) {
        return new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .appendValueReduced(ChronoField.YEAR, 2, 2, Year.now().getValue() - 100)
                .toFormatter();
    }

    public static LocalDate converterData(@NonNull String dateString) {
        if (dateString.isBlank()) {
            return null;
        }

        List<DateTimeFormatter> formatters = new ArrayList<>();
        formatters.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("d/MM/yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("dd/M/yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("d/M/yyyy"));

        formatters.add(getDateTimeFormatterYY("dd/MM/"));
        formatters.add(getDateTimeFormatterYY("d/MM/"));
        formatters.add(getDateTimeFormatterYY("dd/M/"));
        formatters.add(getDateTimeFormatterYY("d/M/"));

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new IllegalArgumentException("Invalid Date: " + dateString);
    }

    public static boolean intersectDate(@NonNull LocalDate newStart, @NonNull LocalDate newEnd, @NonNull LocalDate start, @NonNull LocalDate end) {
        var startBetween = (compare(newStart, start) >= 0 && compare(newStart, end) < 0) || (compare(start, newStart) >= 0 && compare(start, newEnd) < 0);
        var endBetween = (compare(newEnd, start) > 0 && compare(newEnd, end) <= 0) || (compare(end, newStart) > 0 && compare(end, newEnd) <= 0);
        return startBetween || endBetween;
    }

}
