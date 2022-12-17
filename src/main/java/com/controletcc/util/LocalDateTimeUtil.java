package com.controletcc.util;

import lombok.NonNull;

import java.time.LocalDate;
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

    public static String getHoursTitle(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        var pattern = "HH:mm";
        var startStr = localDateTimeToString(start, pattern);
        var endStr = localDateTimeToString(end, pattern);
        return startStr + " - " + endStr;
    }

    public static boolean intersectDate(@NonNull LocalDateTime newStart, @NonNull LocalDateTime newEnd, @NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        var startBetween = (compare(newStart, start) >= 0 && compare(newStart, end) < 0) || (compare(start, newStart) >= 0 && compare(start, newEnd) < 0);
        var endBetween = (compare(newEnd, start) > 0 && compare(newEnd, end) <= 0) || (compare(end, newStart) > 0 && compare(end, newEnd) <= 0);
        return startBetween || endBetween;
    }

    public static boolean date1HourInvalid(LocalDateTime date) {
        return date != null && (date.getMinute() != 0 || date.getSecond() != 0);
    }

    public static boolean validInterval(@NonNull LocalDateTime dateHourStart, @NonNull LocalDateTime dateHourEnd, @NonNull LocalDate limitDateStart, @NonNull LocalDate limitDateEnd, @NonNull Integer limitHourStart, @NonNull Integer limitHourEnd) {
        var hourStart = dateHourStart.getHour();
        var hourEnd = dateHourEnd.getHour();
        var dateStart = dateHourStart.toLocalDate();
        var dateEnd = dateHourEnd.toLocalDate();

        var betweenHourStart = hourStart >= limitHourStart && hourStart < limitHourEnd;
        var betweenHourEnd = hourEnd > limitHourStart && hourEnd <= limitHourEnd;
        var betweenDateStart = LocalDateUtil.compare(dateStart, limitDateStart) >= 0 && LocalDateUtil.compare(dateStart, limitDateEnd) <= 0;
        var betweenDateEnd = LocalDateUtil.compare(dateEnd, limitDateStart) >= 0 && LocalDateUtil.compare(dateEnd, limitDateEnd) <= 0;

        return betweenHourStart && betweenHourEnd && betweenDateStart && betweenDateEnd;
    }
}
