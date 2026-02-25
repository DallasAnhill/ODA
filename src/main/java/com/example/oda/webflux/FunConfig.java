package com.example.oda.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

// 1. 使用函数式编程风格定义路由和处理器, 不需要使用@Controller或@RestController注解

@Configuration
public class FunConfig {

    //http://localhost:8888/annotate
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route().GET("/fun",
                request -> ServerResponse.ok().body("Hello World: Function")).build();
    }

}
