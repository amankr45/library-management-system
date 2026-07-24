package com.aman.LibraryManagementSystem.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class StringToSortDirectionConverter
        implements Converter<String, Sort.Direction> {

    @Override
    public Sort.Direction convert(String source) {

        return Sort.Direction.valueOf(
                source.trim().toUpperCase()
        );
    }
}