package com.controletcc.util;

public class StringUtil {
    public final static String NOT_NUMBER_REGEX = "[^0-9]";

    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static String getOnlyNumbers(String field) {
        if (isNullOrBlank(field))
            return null;
        return field.replaceAll(NOT_NUMBER_REGEX, "");
    }

}
