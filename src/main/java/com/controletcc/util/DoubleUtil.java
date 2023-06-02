package com.controletcc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleUtil {

    private DoubleUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Double roundingHalfUp(int scale, double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(scale, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }
}
