package com.controletcc.dto.csv.type;

public abstract class BaseType<T> {

    public abstract T cast(String value);

    public abstract String toString(Object value);

    public abstract String typeName();
}
