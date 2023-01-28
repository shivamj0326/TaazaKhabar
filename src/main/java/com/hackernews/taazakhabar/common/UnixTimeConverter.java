package com.hackernews.taazakhabar.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.IOException;
import java.time.Instant;

public class UnixTimeConverter extends StdConverter<String, Instant> {

    @Override
    public Instant convert(String value) {
        return Instant.ofEpochSecond(Long.parseLong(value)) ;
    }
}
