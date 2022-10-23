package com.controletcc.converter;

import com.controletcc.util.LocalDateTimeUtil;
import org.modelmapper.AbstractConverter;

import java.time.LocalDateTime;

public class LocalDateTimeConverter extends AbstractConverter<String, LocalDateTime> {
    @Override
    protected LocalDateTime convert(String date) {
        return LocalDateTimeUtil.stringToLocalDateTime(date, "yyyy-MM-dd'T'HH:mm:ss");
    }
}
