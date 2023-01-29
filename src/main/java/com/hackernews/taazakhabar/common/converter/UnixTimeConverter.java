package com.hackernews.taazakhabar.common.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UnixTimeConverter extends StdConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String value) {
        return Instant.ofEpochSecond(Long.parseLong(value)).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}
