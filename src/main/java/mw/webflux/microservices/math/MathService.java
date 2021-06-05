package mw.webflux.microservices.math;

import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class MathService {

    public Mono<MathResponse> square(int number) {
        return Mono.fromSupplier(() -> MathResponse.from(Instant.now(), number * number));
    }

    public Mono<MathResponse> squareWithValidation(int input) {

        return Mono.just(input).handle((it, sink) -> {
            if (it > 50) {
                sink.error(new IllegalArgumentException("Input number is > than 50!"));
            } else {
                sink.next(it);
            }
        }).map(it -> (Integer) it).map((it) -> MathResponse.from(Instant.now(), it * it));
    }

    public Flux<MathResponse> generateMultiplicationTable(int input) {
        return Flux.range(1, 10).map(val -> MathResponse.from(Instant.now(), val * input))
                   //blocking sleep
                   //.doOnNext((res)-> Sleeper.sleepSecconds(1))
                   //non blocking sleep
                   .delayElements(Duration.ofSeconds(1));
    }

    public Mono<MathResponse> multiply(Mono<MultiplicityRequest> multiplicityRequest) {
        return multiplicityRequest.map(it -> it.getFirst() * it.getSecond()).map(it -> MathResponse.from(Instant.now(), it));
    }


}
