package com.example.oda.reactive.reactivestream;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {

    private Flow.Subscription subscription;

    public ReactiveProcessor() {
        super(Executors.newSingleThreadExecutor(), 5); //使用单线程执行器，缓冲区大小为5。这是processor作为'publisher'时的缓冲区，
    }

    //实现subscriber接口的方法
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("Processor建立订阅关系");
        this.subscription = subscription;
        this.subscription.request(1); // 请求一个数据
    }

    @Override
    public void onNext(String item) {
        System.out.println("Processor接收到数据: " + item);//数据发送给subscriber
        //中间处理：将字符串转换为大写
        this.submit(item.toUpperCase());
        //背压
        subscription.request(1); // 请求下一个数据
        System.out.println("---处理器内部缓冲区剩余容量: " + this.estimateMaximumLag()); //estimateMaximumLag方法返回当前处理器内部缓冲区中未处理的数据数量，即发布者提交的数据数量减去订阅者请求的数据数量
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("发生错误: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("数据流完成");
    }

    /* 当你调用 processor.submit(data)
        1. 数据先进入内部队列
        2. 如果队列长度 <= 5，立即接受
        3. 如果队列长度 > 5，可能：
            - 如果下游处理快，继续接受
            - 如果下游处理慢，开始阻塞或丢弃
            - 但estimateMaximumLag()会继续增加，反映未处理的数据数量

       阈值5是触发背压的临界点，不是硬性上限
       积压数据可以临时超过阈值
       当持续超过阈值时，才会触发真正的背压（阻塞/丢弃）

       submit(): 线程阻塞等待
       offer(): 立即返回失败，可配置处理策略
     */
}
