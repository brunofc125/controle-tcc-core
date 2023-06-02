package com.controletcc.util;

import com.controletcc.converter.LocalDateConverter;
import com.controletcc.converter.LocalDateTimeConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperUtil {
    private static final ModelMapper modelMapper;

    static {
        modelMapper = buildMap(MatchingStrategies.STRICT, false);
    }

    private ModelMapperUtil() {
        throw new IllegalStateException("Utility Class, do not instantiate it.");
    }

    private static ModelMapper buildMap(MatchingStrategy matchingStrategy, boolean preferNestedProperties) {
        var modelMapper = new ModelMapper();
        modelMapper.createTypeMap(String.class, LocalDate.class);
        modelMapper.createTypeMap(String.class, LocalDateTime.class);
        modelMapper.addConverter(new LocalDateConverter());
        modelMapper.addConverter(new LocalDateTimeConverter());
        modelMapper.getConfiguration().setMatchingStrategy(matchingStrategy).setPreferNestedProperties(preferNestedProperties);
        return modelMapper;
    }

    private static ModelMapper chooseMap(MatchingStrategy matchingStrategy, Boolean preferNestedProperties) {
        ModelMapper modelMapper = ModelMapperUtil.modelMapper;
        if (matchingStrategy != null || preferNestedProperties != null) {
            if (matchingStrategy == null) {
                matchingStrategy = ModelMapperUtil.modelMapper.getConfiguration().getMatchingStrategy();
            }
            if (preferNestedProperties == null) {
                preferNestedProperties = ModelMapperUtil.modelMapper.getConfiguration().isPreferNestedProperties();
            }
            modelMapper = buildMap(matchingStrategy, preferNestedProperties);
        }
        return modelMapper;
    }

    public static <D, T> D map(final T entity, Class<D> outClass, MatchingStrategy matchingStrategy, Boolean preferNestedProperties) {
        ModelMapper modelMapper = chooseMap(matchingStrategy, preferNestedProperties);
        return modelMapper.map(entity, outClass);
    }

    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass, MatchingStrategy matchingStrategy, Boolean preferNestedProperties) {
        ModelMapper modelMapper = chooseMap(matchingStrategy, preferNestedProperties);
        if (entityList != null) {
            return entityList.stream().map(entity -> modelMapper.map(entity, outCLass)).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public static <S, D> D map(final S source, D destination, MatchingStrategy matchingStrategy, Boolean preferNestedProperties) {
        ModelMapper modelMapper = chooseMap(matchingStrategy, preferNestedProperties);
        modelMapper.map(source, destination);
        return destination;
    }

    public static <S, D> D map(final S source, Type genericType, MatchingStrategy matchingStrategy, Boolean preferNestedProperties) {
        ModelMapper modelMapper = chooseMap(matchingStrategy, preferNestedProperties);
        return modelMapper.map(source, genericType);
    }

    public static <D, T> D map(final T entity, Class<D> outClass) {
        return entity != null ? map(entity, outClass, null, null) : null;
    }

    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return mapAll(entityList, outCLass, null, null);
    }

    public static <S, D> D map(final S source, D destination) {
        return map(source, destination, null, null);
    }

    public static <S, D> D map(final S source, Type genericType) {
        return map(source, genericType, null, null);
    }

}
