package com.spring.baseproject.components.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class JSONProcessor {
    @Autowired
    private ResourceLoader resourceLoader;

    public Map<String, String> parseFromResourceFiles(String path) throws IOException {
        InputStream inputStream = resourceLoader
                .getResource("classpath:" + path)
                .getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>(){});
    }
}
