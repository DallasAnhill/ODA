package com.example.oda.reactive.reactivestream;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveProcessorDemoOffer_NotWorking {
    public static void main(String[] args) {
        //1. 创建发布者publisher
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>(
                Executors.newSingleThreadExecutor(),
                5, // 缓冲区大小
                (subscriber, item) -> {
                    // 这个handler在缓冲区满时被调用
                    System.out.println("⚡ 立即背压! 数据被拒绝: " + item);
                }
        );

        //2. 创建处理器processor
        ReactiveProcessor processor = new ReactiveProcessor();

        //3. 创建订阅关系：publisher和processor建立订阅关系
        publisher.subscribe(processor);

        //4. 创建订阅者subscriber
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<String>() {
            Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                System.out.println("最终订阅者: 建立订阅关系");
                this.subscription = subscription;
                subscription.request(1); //请求一个数据(第一次请求数据，后续在onNext方法中继续请求)
            }

            @Override
            public void onNext(String item) {
                System.out.println("最终订阅者: 接收到数据 " + item);
                //处理数据
                subscription.request(10); //继续请求10个数据 (背压)
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("最终订阅者: 发生错误: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("最终订阅者: 数据流完成");
            }
        };

        //5. 创建订阅关系：processor和subscriber建立订阅关系
        processor.subscribe(subscriber);

        //6. 发布者publisher发布数据
        for (int i = 0; i < 100; i++) {
            System.out.println("发布者: 发布数据 data" + i);
            publisher.offer("data" + i, (subscriber1, item) -> {
                System.out.println("数据 " + item + " 被拒绝，执行自定义处理策略");
                //这里可以选择丢弃数据、重试发布、或者其他处理逻辑
                return false; //返回false表示丢弃数据
            });
        }


        //7. Reactive Stream的特点：异步非阻塞、背压机制、数据流完成通知。 需要阻塞主线程，否则程序会立即退出，无法看到数据流的处理结果
        try {
            Thread.currentThread().join(10000L); //阻塞主线程10秒钟，等待数据流处理完成
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
