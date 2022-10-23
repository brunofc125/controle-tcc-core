package com.controletcc.converter;

import com.controletcc.util.LocalDateUtil;
import org.modelmapper.AbstractConverter;

import java.time.LocalDate;

public class LocalDateConverter extends AbstractConverter<String, LocalDate> {
    @Override
    protected LocalDate convert(String date) {
        return LocalDateUtil.stringToLocalDate(date, "yyyy-MM-dd");
    }
}
