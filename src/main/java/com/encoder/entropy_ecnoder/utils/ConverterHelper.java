package com.encoder.entropy_ecnoder.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;

@Converter
public class ConverterHelper implements AttributeConverter<HashMap<String, Object>,String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterHelper.class);

    @Override
    public HashMap<String, Object> convertToEntityAttribute(String attribute) {
        if (attribute == null) {
            return new HashMap<>();
        }
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(attribute, HashMap.class);
        }
        catch (IOException e) {
            LOGGER.error("Convert error while trying to convert string(JSON) to map data structure.");
        }
        return new HashMap<>();
    }

    @Override
    public String convertToDatabaseColumn(HashMap<String, Object> stringObjectMap) {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(stringObjectMap);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("Could not convert map to json string.");
            return null;
        }
    }
}