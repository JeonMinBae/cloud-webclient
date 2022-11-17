package com.example.webclienttutorial.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static final ApplicationContextHolder contextHolder = new ApplicationContextHolder();

    public static Object getBean(final String name) {
        if (contextHolder.context == null) {
            return null;
        }
        return contextHolder.context.getBean(name);
    }

    public static ObjectMapper getObjectMapper() {
        return (ObjectMapper) getBean("objectMapper");
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        contextHolder.context = applicationContext;
    }

    private static class ApplicationContextHolder {
        private ApplicationContext context;
    }

}
