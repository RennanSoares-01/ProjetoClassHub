package com.classhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClassHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassHubApplication.class, args);
    }
}
