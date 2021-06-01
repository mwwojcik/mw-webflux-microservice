package mw.webflux.microservices.math;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/square/{input}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<MathResponse> multiply(@PathVariable int input) {
        return mathService.multiplicity(input);
    }

    @GetMapping(value = "/table/{input}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MathResponse> table(@PathVariable int input) {
        return mathService.generateMultiplicationTable(input);
    }
}
