package com.example.oda.retry.config;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.policy.SimpleRetryPolicy;

@Component
public class AppRetryListenerSupport implements RetryListener {

    private final SimpleRetryPolicy retryPolicy;
    private final ExponentialBackOffPolicy backOffPolicy;

    @Autowired
    public AppRetryListenerSupport(SimpleRetryPolicy retryPolicy, ExponentialBackOffPolicy backOffPolicy) {
        this.retryPolicy = retryPolicy;
        this.backOffPolicy = backOffPolicy;
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("重试失败，执行onError方法: " + throwable.getMessage());

        if(throwable instanceof ResourceAccessException) {
            System.out.println("POD访问异常，可能是网络问题导致的连接失败 - 将最大重试次数设置为10");
            // 动态调整重试策略的最大重试次数
            retryPolicy.setMaxAttempts(10); //管用
            backOffPolicy.setInitialInterval(1000L); //不管用
            // ExponentialBackOffPolicy的setInitialInterval不管用
            // FixedBackOffPolicy的setBackOffPeriod管用, 但是不能设置multipler
        } else if(throwable instanceof HttpClientErrorException) {
            System.out.println("HTTP客户端错误，可能是请求参数错误或资源不存在");
        } else if(throwable instanceof IllegalArgumentException) {
            System.out.println("非法参数异常，可能是方法参数错误");
        }

    }

}
