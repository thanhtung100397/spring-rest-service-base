package com.spring.baseproject.events_handle;

public interface ApplicationEventHandle {
    void handleEvent() throws Exception;
    String startMessage();
    String successMessage();
}
