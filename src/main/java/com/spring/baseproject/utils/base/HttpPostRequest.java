package com.spring.baseproject.utils.base;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpPostRequest {
    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private String url;

    public HttpPostRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.headers = new HttpHeaders();
    }

    public HttpPostRequest setContentType(MediaType mediaType) {
        headers.setContentType(mediaType);
        return this;
    }

    public HttpPostRequest withUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpPostRequest addToHeader(String key, String value) {
        if (key != null && value != null) {
            headers.add(key, value);
        }
        return this;
    }

    public FormDataBodyRequestExecutor setFormDataBody(LinkedMultiValueMap<String, String> map) {
        return new FormDataBodyRequestExecutor(map);
    }

    public JsonBodyRequestExecutor setJsonBody(Object object) {
        if (object != null) {
            this.headers.add("Content-Type", "application/json; charset=UTF-8");
        }
        return new JsonBodyRequestExecutor(object);
    }

    public class FormDataBodyRequestExecutor {
        private MultiValueMap<String, String> body;

        FormDataBodyRequestExecutor(MultiValueMap<String, String> body) {
            this.body = body;
        }

        public <T> T execute(Class<T> responseClazz) {
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
            return restTemplate.postForObject(url, httpEntity, responseClazz);
        }
    }

    public class JsonBodyRequestExecutor {
        private Object body;

        JsonBodyRequestExecutor(Object body) {
            this.body = body;
        }

        public <T> T execute(Class<T> responseClazz) {
            HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);
            return restTemplate.postForObject(url, httpEntity, responseClazz);
        }
    }
}
