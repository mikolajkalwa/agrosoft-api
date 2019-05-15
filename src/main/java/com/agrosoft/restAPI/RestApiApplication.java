package com.agrosoft.restAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RestApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

}
