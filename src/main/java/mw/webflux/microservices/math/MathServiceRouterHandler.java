package mw.webflux.microservices.math;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
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

    public Mono<ServerResponse> multiplicationTableHandler(ServerRequest serverRequest) {
        var input = Integer.valueOf(serverRequest.pathVariable("input"));
        var squareMono = this.mathService.generateMultiplicationTable(input);
        return ServerResponse.ok().body(squareMono, MathResponse.class);
    }

    public Mono<ServerResponse> multiplicationTableStreamHandler(ServerRequest serverRequest) {
        var input = Integer.valueOf(serverRequest.pathVariable("input"));
        var squareMono = this.mathService.generateMultiplicationTable(input);
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(squareMono, MathResponse.class);
    }

    public Mono<ServerResponse> multipicity(ServerRequest serverRequest) {
        var input = serverRequest.bodyToMono(MultiplicityRequest.class);
        var squareMono = this.mathService.multiply(input);
        return ServerResponse.ok().body(squareMono, MathResponse.class);
    }

}
