package com.example.oda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class OdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdaApplication.class, args);
    }

}
