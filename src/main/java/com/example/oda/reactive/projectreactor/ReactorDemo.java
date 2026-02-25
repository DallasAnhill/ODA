package com.example.oda.reactive.projectreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Stream;

public class ReactorDemo {
    public static void main(String[] args) {
        //Mono和Flux是Reactor库中的两个核心类型，分别表示单值和多值的异步数据流。都属于Publisher
        //Mono：表示一个单值的异步数据流，类似于Java中的Optional或Future。它可以包含一个值，也可以是空的。
        //Flux：表示一个多值的异步数据流，类似于Java中的Stream。它可以包含零个、一个或多个值。

        //--------Mono--------
        Mono.empty().subscribe(System.out::println);

        Mono.just("Hello").subscribe(System.out::println);

        //Mono.doOnNext()方法：在数据流中添加一个操作，但需要subscribe()方法来触发数据流的执行
        Mono<String> mono = Mono.just("Hello World")
                .doOnNext(data -> {
                    System.out.println("doOnNext: " + data); //在数据流中添加一个操作，但需要subscribe()方法来触发数据流的执行
                });
        mono.subscribe();


        //--------Flux--------
        Flux.just(1, 2, 3, 4, 5)
                .filter(i -> i % 2 == 0) //过滤出偶数
                .map(i -> i * i) //将偶数平方
                .subscribe(System.out::println); //输出结果: 4, 16


        Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5)).subscribe(System.out::println);

        Flux.fromArray(new Integer[]{1, 2, 3, 4, 5}).subscribe(System.out::println);

        Flux.fromStream(Stream.of(1, 2, 3, 4, 5)).subscribe(System.out::println);

        Flux.range(1, 5).subscribe(System.out::println);


        /*
        案例：
        2*0=0
        2*1=2
        2*2=4
        2*3=6
        ...
        2*9=18
         */
        Flux.generate(
                        () -> 0,
                        (i, sink) -> {
                            sink.next("2*" + i + "=" + (2 * i));
                            if (i == 10) {
                                sink.complete();
                            };
                            return i + 1;
                        }
                )
                .subscribe(System.out::println); //必须使用subscribe()方法来触发数据流的执行，否则上面的generate方法不会被调用，数据流也不会被生成和处理。
    }
}
