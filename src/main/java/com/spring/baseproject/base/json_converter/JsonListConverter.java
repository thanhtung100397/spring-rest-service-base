package com.spring.baseproject.base.json_converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.spring.baseproject.utils.base.JacksonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonListConverter implements AttributeConverter<List<?>, String>  {
    private static final Logger logger = LoggerFactory.getLogger(JsonListConverter.class);

    @Override
    public String convertToDatabaseColumn(List<?> attribute) {
        if (attribute == null) {
            return "[]";
        }
        try {
            return JacksonObjectMapper.getInstance().writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert attribute " + attribute + " to json", e);
            return "[]";
        }
    }

    @Override
    public List<?> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return Collections.emptyList();
        }
        try {
            return JacksonObjectMapper.getInstance().readValue(dbData, new TypeReference<List<?>>(){});
        } catch (IOException e) {
            logger.error("Failed to convert json " + dbData + " to List<?>", e);
            return Collections.emptyList();
        }
    }
}
