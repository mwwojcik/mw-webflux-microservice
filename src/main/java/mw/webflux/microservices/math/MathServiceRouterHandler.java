package mw.webflux.microservices.math;

import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class MathServiceRouterHandler {

    private MathService mathService;

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        var input = Integer.valueOf(serverRequest.pathVariable("input"));
        var squareMono = this.mathService.square(input);
        return ServerResponse.ok().body(squareMono, MathResponse.class);
    }
}
