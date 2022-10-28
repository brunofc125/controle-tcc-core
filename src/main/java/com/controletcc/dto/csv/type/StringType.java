package com.controletcc.dto.csv.type;

public class StringType extends BaseType<String> {

    @Override
    public String cast(String value) {
        return value;
    }

    @Override
    public String toString(Object value) {
        return (String) value;
    }

    @Override
    public String typeName() {
        return "texto";
    }
}
