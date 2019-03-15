package com.spring.baseproject.events_handle.rbac.http_methods_persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.baseproject.annotations.event.EventHandler;
import com.spring.baseproject.events_handle.ApplicationEvent;
import com.spring.baseproject.events_handle.ApplicationEventHandle;
import com.spring.baseproject.modules.rbac.models.entities.ApiMethod;
import com.spring.baseproject.modules.rbac.repositories.ApiMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP, order = 2)
public class HttpMethodsPersistenceService implements ApplicationEventHandle {

    @Value("${application.rbac.http-methods.path:rbac/http-methods.json}")
    private String httpMethodPriorityFilePath;
    @Value("${application.rbac.refresh:false}")
    private boolean isActive;

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ApiMethodRepository apiMethodRepository;

    @Override
    public String startMessage() {
        return "Start persisting http methods...";
    }

    @Override
    public String successMessage() {
        if (isActive) {
            return "Http methods persistence...OK";
        } else {
            return "Http methods persistence...SKIPPED";
        }
    }

    @Override
    public void handleEvent() throws Exception {
        if (isActive) {
            InputStream inputStream = resourceLoader
                    .getResource("classpath:" + httpMethodPriorityFilePath)
                    .getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Integer> httpMethodPriority = objectMapper
                    .readValue(inputStream, new TypeReference<Map<String, Integer>>() {
                    });

            Set<ApiMethod> apiMethods = new HashSet<>();
            for (HttpMethod httpMethod : HttpMethod.values()) {
                apiMethods.add(new ApiMethod(httpMethod.name(), httpMethodPriority.get(httpMethod.name())));
            }
            apiMethodRepository.saveAll(apiMethods);
        }
    }
}
