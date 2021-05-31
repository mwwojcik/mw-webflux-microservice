package mw.webflux.microservices.math;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive-math")
public class MathController {

    @Autowired
    private MathService mathService;

    @GetMapping("/square/{input}")
    public Mono<MathResponse> multiply(@PathVariable int input) {
        return mathService.multiplicity(input);
    }

    @GetMapping("/table/{input}")
    public Flux<MathResponse> table(@PathVariable int input) {
        return mathService.generateMultiplicationTable(input);
    }
}
