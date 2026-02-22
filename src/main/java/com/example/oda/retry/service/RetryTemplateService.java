package com.example.oda.retry.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RetryTemplateService {
    public String getRetryTemplateNumber(int port) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(formatter);
        System.out.println("执行开始: " + formattedTime);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + port;
        String response = restTemplate.getForObject(url, String.class);

        System.out.println("执行结束: " + formattedTime);

        return "success";
    }
}
