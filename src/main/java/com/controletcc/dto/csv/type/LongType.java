package com.controletcc.dto.csv.type;

public class LongType extends BaseType<Long> {

    @Override
    public Long cast(String value) {
        return value != null ? Long.parseLong(value.trim()) : null;
    }

    @Override
    public String toString(Object value) {
        return value.toString();
    }

    @Override
    public String typeName() {
        return "numérico";
    }
}
