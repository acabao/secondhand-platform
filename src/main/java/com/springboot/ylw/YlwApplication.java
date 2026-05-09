package com.springboot.ylw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YlwApplication {

    public static void main(String[] args) {
        SpringApplication.run(YlwApplication.class, args);
    }

}
