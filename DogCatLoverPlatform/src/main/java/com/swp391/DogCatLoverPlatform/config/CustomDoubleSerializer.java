package com.swp391.DogCatLoverPlatform.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DecimalFormat;

public class CustomDoubleSerializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Double value = Double.valueOf(jp.getValueAsString());
        String pattern = ".##";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return Double.valueOf( myFormatter.format(value));
    }
}
