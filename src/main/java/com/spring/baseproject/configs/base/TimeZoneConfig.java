package com.spring.baseproject.configs.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {
    public static final Logger logger = LoggerFactory.getLogger(TimeZoneConfig.class);

    @Value("${application.timezone}")
    private String serverTimeZone;

    @PostConstruct
    public void initTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(serverTimeZone));
        logger.info("Spring boot application running in '" + serverTimeZone + "' timezone :" + new Date().toString());
    }
}
