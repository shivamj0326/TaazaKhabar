package com.hackernews.taazakhabar.common;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDateTime;


public class InstantToStringConverter extends StdConverter<LocalDateTime, String> {

    @Override
    public String convert(LocalDateTime value) {
        return String.valueOf(value.toString()) ;
    }
}

