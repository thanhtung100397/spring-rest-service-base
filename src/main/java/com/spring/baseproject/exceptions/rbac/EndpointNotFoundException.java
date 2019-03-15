package com.spring.baseproject.exceptions.rbac;

public class EndpointNotFoundException extends Exception {
    public EndpointNotFoundException() {
        super("Endpoint not found");
    }
}
