package com.controletcc.annotation;

import com.controletcc.dto.csv.type.BaseType;
import com.controletcc.dto.csv.type.StringType;
import com.controletcc.dto.enums.CsvType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvColumn {

    public String name();

    public CsvType type();

    public Class<? extends Enum> enumClass() default Enum.class;

    public Class<? extends BaseType<?>> listClass() default StringType.class;

}
