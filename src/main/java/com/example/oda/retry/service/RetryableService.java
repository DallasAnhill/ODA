package com.example.oda.retry.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RetryableService {

    /* @Retryable: 标记当前方法使用重试机制
         value: 触发重试机制的条件，当遇到Exception时，会重试
         maxAttempts: 设置最大重试次数，默认为3次
         delay: 重试延迟时间，单位毫秒，即距离上一次重试方法的间隔
         multiplier: delay重试延迟时间的间隔倍数，即第一次为5秒，第二次为5乘以2为10秒，依此类推

         下面例子中，一共会执行3次：
            第一次执行时num为0，抛出异常，

            等待5秒后重试，
            第二次执行时num仍然为0，抛出异常，

            等待10秒后重试，
            第三次执行时num仍然为0，抛出异常，

            达到最大重试次数后调用@Recover标记的方法进行恢复操作
            如果没有@Recover标记的方法，重试失败后会抛出异常
     */

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    public int getRetryNumber(int num) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(formatter);
        System.out.println("getRemainingAmount =====" + formattedTime);
        System.out.println("执行开始： 现在num是 " + num);

        if(num <= 0) {
            num = num + 2; //这行代码不管用：每次重试，num的值会被重置为传入的值
            throw new Exception("数量不够");
        }

        System.out.println("执行结束： getRemainingAmount =====" + formattedTime);
        System.out.println("执行结束： 现在num是 " + num);

        return num;
    }


    @Recover
    public int recover(Exception e) {
        System.out.println("重试失败，执行恢复方法: " + e.getMessage());
        return -1; // 返回一个默认值或者进行其他的恢复操作
    }
}
