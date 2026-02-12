package com.example.oda.retry.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    /*
        1. setBackOffPeriod设置重试间隔2秒。
        2. setMaxAttempts设置最大重试次数2次。
        3. registerListener注册tryTemplate监听器。
    */

    @Bean
    public SimpleRetryPolicy simpleRetryPolicy() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // 设置默认最大重试次数为3次
        return retryPolicy;
    }

    @Bean
    public ExponentialBackOffPolicy fixedBackOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval (1000L); // 设置重试间隔为5秒
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setMaxInterval (10000L);
        return backOffPolicy;
    }

    @Bean
    public RetryTemplate retryTemplate(SimpleRetryPolicy retryPolicy, ExponentialBackOffPolicy exponentialBackOffPolicy, AppRetryListenerSupport appRetryListenerSupport) {
        RetryTemplate retryTemplate = new RetryTemplate();

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        // 使用注入的 listener bean，而不是 new 一个实例，这样 listener 可以注入并修改 retryPolicy
        retryTemplate.registerListener(appRetryListenerSupport);

        return retryTemplate;
    }
}
