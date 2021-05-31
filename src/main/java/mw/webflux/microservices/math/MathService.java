package mw.webflux.microservices.math;

import java.time.Instant;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MathService {

    public Mono<MathResponse> multiplicity(int number) {
        return Mono.fromSupplier(() -> MathResponse.from(Instant.now(), number * number));
    }

    public Flux<MathResponse> generateMultiplicationTable(int input) {
        return Flux.range(1, 10).map(val -> MathResponse.from(Instant.now(), val * input));
    }
}
