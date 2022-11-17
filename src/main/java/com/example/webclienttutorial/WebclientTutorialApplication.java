package com.example.webclienttutorial;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebclientTutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebclientTutorialApplication.class, args);
        // jaxb-ri runtime 최적화 끄기
        System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");

    }

    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
