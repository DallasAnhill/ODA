package com.example.oda.retry.controller;

import com.example.oda.retry.service.RetryTemplateService;
import com.example.oda.retry.service.RetryableService;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {
    private final RetryableService retryableService;
    private final RetryTemplateService retryTemplateService;
    private final RetryTemplate retryTemplate;

    public RetryController(RetryableService retryableService, RetryTemplateService retryTemplateService,
                           RetryTemplate retryTemplate) {
        this.retryableService = retryableService;
        this.retryTemplateService = retryTemplateService;
        this.retryTemplate = retryTemplate;
    }


    @GetMapping("/retryable/{num}")
    public String retryableTest(@PathVariable int num) throws Exception {
        int retryNum = retryableService.getRetryNumber(num);
        System.out.println("剩余数量" + retryNum);
        return "success";
    }


    @GetMapping("/temp/{port}")
    public String retryTemplateTest(@PathVariable int port) throws Exception {
        try{
            String result = retryTemplate.execute(
                    arg -> retryTemplateService.getRetryTemplateNumber(port));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
