package mw.webflux.microservices.math;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

public class MathRouterFunctionConfiguration {

    @Bean
    public RouterFunction<ServerResponse> mathServiceRouterFunction(MathServiceRouterHandler mathServiceRouterHandler) {
        return RouterFunctions.route()
                              .GET("/reactive-math/router/square/{input}", mathServiceRouterHandler::squareHandler)
                              .GET("/reactive-math/router/table/{input}",mathServiceRouterHandler::multiplicationTableHandler)
                              .GET("/reactive-math/router/table-stream/{input}",mathServiceRouterHandler::multiplicationTableStreamHandler)
                              .POST("/reactive-math/router/multiply",mathServiceRouterHandler::multipicity)
                              .build();
    }

    @Bean
    public MathServiceRouterHandler mathServiceRouterHandler(MathService mathService) {
        return new MathServiceRouterHandler(mathService);
    }
}
