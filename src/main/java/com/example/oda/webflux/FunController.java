package com.example.oda.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FunController {
    @GetMapping("/annotate")
    public Mono<String> annotate() {
        return Mono.just("Hello World: Annotate");
    }
}
