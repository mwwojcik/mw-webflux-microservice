package mw.webflux.microservices.math;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.AbstractJackson2Encoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Mono<MathResponse> square(@PathVariable int input) {
        return mathService.square(input);
    }


    @GetMapping(value = "/validated-square/{input}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<MathResponse> squareWithValidation(@PathVariable int input) {
        return mathService.squareWithValidation(input);
    }

    @GetMapping(value = "/table/{input}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MathResponse> table(@PathVariable int input) {
        return mathService.generateMultiplicationTable(input);
    }

    @PostMapping(value = "/multiply")
    public Mono<MathResponse> multiply(@RequestBody Mono<MultiplicityRequest> multiplicityRequest){
        return mathService.multiply(multiplicityRequest);
    }
}


/*
* UWAGA!!
*
* Jeśli nie zostanie ustawiony produces = MediaType.TEXT_EVENT_STREAM_VALUE wtedy mimot tego że obsługa żądania jest reaktywna
* to spodziewany typ wyjściowy to JSON, w klasie AbstractJackson2Encoder następuje blokujące kolekcjonowanie wszystkich elementów
* i zwrot w postaci jednej listy!
*
* else { // non-streaming
				ResolvableType listType = ResolvableType.forClassWithGenerics(List.class, elementType);
				return Flux.from(inputStream)
						.collectList()
						.map(list -> encodeValue(list, bufferFactory, listType, mimeType, hints))
						.flux();
			}
* */