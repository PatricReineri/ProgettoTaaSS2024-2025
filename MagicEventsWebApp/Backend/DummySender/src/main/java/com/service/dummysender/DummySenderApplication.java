package com.service.dummysender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DummySenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DummySenderApplication.class, args);
    }
}
