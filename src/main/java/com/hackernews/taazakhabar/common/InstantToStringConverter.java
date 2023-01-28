package com.hackernews.taazakhabar.common;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class InstantToStringConverter extends StdConverter<Instant, String> {

    @Override
    public String convert(Instant value) {
        return String.valueOf(LocalDateTime.ofInstant(value, ZoneOffset.UTC)) ;
    }
}

