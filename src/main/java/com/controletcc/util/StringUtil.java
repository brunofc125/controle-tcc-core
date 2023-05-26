package com.controletcc.util;

import java.text.Normalizer;

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

    public static String getUsernameByFullName(String fullName) {
        if (fullName == null || fullName.trim().length() == 0) {
            return "user";
        }
        fullName = fullName.trim();
        var partName = fullName.split("\\s+");
        var firstName = partName[0];
        return Normalizer.normalize(firstName, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    public static boolean equalsTrimIgnoreCase(String str1, String str2) {
        if (str1 != null && str2 != null) {
            return str1.trim().equalsIgnoreCase(str2.trim());
        } else {
            return str1 == null && str2 == null;
        }
    }

}
