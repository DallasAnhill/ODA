package com.example.oda.reactive.reactivestream;

import java.sql.SQLOutput;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveStreamDemo {
    public static void main(String[] args) {
//        1. 创建一个发布者publisher
//        Flow.Publisher publisher = new Flow.Publisher() {
//            @Override
//            public void subscribe(Flow.Subscriber subscriber) {
//
//            }
//        };

        //1. 创建一个发布者publisher (直接使用publisher的实现类SubmissionPublisher)
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        //2. 创建一个订阅者subscriber
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<Integer>() {

            Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                System.out.println("建立订阅关系");
                this.subscription = subscription;
                subscription.request(1); //请求一个数据(第一次请求数据，后续在onNext方法中继续请求)
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("接收到数据: " + item);
                //处理数据
                subscription.request(10); //继续请求10个数据 (背压)
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("发生错误: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("数据流完成");
            }
        };

        //3. 发布者publisher和订阅者subscriber建立订阅关系
        publisher.subscribe(subscriber);

        //4. 发布者publisher发布数据
        for (int i = 0; i < 100; i++) {
            publisher.submit(i);
        }


        //5. Reactive Stream的特点：异步非阻塞、背压机制、数据流完成通知。 需要阻塞主线程，否则程序会立即退出，无法看到数据流的处理结果
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
