package com.controletcc.util;

import com.controletcc.model.entity.base.EventTime;

import java.time.LocalDate;
import java.util.List;

public class EventTimeUtil {

    private EventTimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T extends EventTime> boolean isDataInicialEmpty(List<T> events) {
        return events.stream().anyMatch(EventTime::isDataInicialEmpty);
    }

    public static <T extends EventTime> boolean isDataFinalEmpty(List<T> events) {
        return events.stream().anyMatch(EventTime::isDataFinalEmpty);
    }

    public static <T extends EventTime> boolean isDataInicialEqualOrAfterDataFinal(List<T> events) {
        return events.stream().anyMatch(EventTime::isDataInicialEqualOrAfterDataFinal);
    }

    public static <T extends EventTime> boolean isDataInicialAndDataFinalDifferentDays(List<T> events) {
        return events.stream().anyMatch(EventTime::isDataInicialAndDataFinalDifferentDays);
    }

    public static <T extends EventTime> boolean isHourInvalid(List<T> events) {
        return events.stream().anyMatch(e -> e.isDataInicialHourInvalid() || e.isDataFinalHourInvalid());
    }

    public static <T extends EventTime> boolean isDataInicialHourInvalid(List<T> events) {
        return events.stream().anyMatch(EventTime::isDataInicialHourInvalid);
    }

    public static <T extends EventTime> boolean isDataFinalHourInvalid(List<T> events) {
        return events.stream().anyMatch(EventTime::isDataFinalHourInvalid);
    }

    public static <T extends EventTime> boolean invalidInterval(List<T> events, LocalDate limitDateStart, LocalDate limitDateEnd, Integer limitHourStart, Integer limitHourEnd) {
        return events.stream().anyMatch(e -> e.invalidInterval(limitDateStart, limitDateEnd, limitHourStart, limitHourEnd));
    }

    public static <T extends EventTime> boolean invalidInterpolation(List<T> events) {
        var eventsDataFilled = events.stream().filter(e -> !e.isEmpty()).toList();
        return eventsDataFilled.stream().anyMatch(e -> e.invalidInterpolation(eventsDataFilled));
    }

}
