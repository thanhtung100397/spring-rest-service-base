package com.spring.baseproject.utils.base;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON to Object and Object to JSON converter
 */
public class JacksonObjectMapper {
    public static ObjectMapper instance;

    static {
        try {
            instance = new ObjectMapper();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Jackson object mapper initial failed");
        }
    }

    public static ObjectMapper getInstance() {
        return instance;
    }
}
