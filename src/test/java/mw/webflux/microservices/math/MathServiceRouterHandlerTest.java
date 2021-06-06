package mw.webflux.microservices.math;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = MathRouterFunctionConfiguration.class)
@Import({MathRouterFunctionConfiguration.class})
@Slf4j
class MathServiceRouterHandlerTest {

    @Autowired
    private WebClient webClient;

    @DisplayName("Should return square of given number via blocking request")
    @Test
    void shouldReturnSquareOfGivenNumberViaBlockingRequest() {

        var result = webClient.get()
                              .uri("/reactive-math/router/square/{input}", 7)
                              .retrieve()
                              .bodyToMono(MathResponse.class)
                              .block();
        Assertions.assertThat(result.getValue()).isEqualTo(49);
    }

    @DisplayName("Should return square of given number via non blocking way")
    @Test
    void shouldReturnSquareOfGivenNumberViaNonBlockingWay() {
        var mathResponseMono = webClient.get()
                                        .uri("/reactive-math/router/square/{input}", 9)
                                        .retrieve()
                                        .bodyToMono(MathResponse.class);
        //non blocking way, server could not return response
        //we have to use Reactor Producer testing library - Step Verifier
        StepVerifier.create(mathResponseMono).expectNextMatches(it -> it.getValue() == 81).verifyComplete();
    }

    @DisplayName("Should return multiplicity table for given number")
    @Test
    void shouldReturnMultiplicityTableForGivenNumber() {
        var mathResponseFlux = webClient.get()
                                        .uri("/reactive-math/router/table/{input}", 7)
                                        .retrieve()
                                        .bodyToFlux(MathResponse.class)
                                        .doOnNext((it) -> log.info(String.valueOf(it.getValue())));

        StepVerifier.create(mathResponseFlux).expectNextCount(10).verifyComplete();
    }

    @DisplayName("Should return multiplicity table stream for given number")
    @Test
    void shouldReturnMultiplicityTableStreamForGivenNumber() {
        var mathResponseFlux = webClient.get()
                                        .uri("/reactive-math/router/table-stream/{input}", 7)
                                        .retrieve()
                                        .bodyToFlux(MathResponse.class)
                                        .doOnNext((it) -> log.info(String.valueOf(it.getValue())));

        StepVerifier.create(mathResponseFlux).expectNextCount(10).verifyComplete();

    }

    @DisplayName("Should multiply given numbers")
    @Test
    void shouldMultiplyGivenNumbers() {
        var mathResponseMono = webClient.post()
                                        .uri("/reactive-math/router/multiply")
                                        //if producer type use body(), else bodyValue()
                                        .bodyValue(new MultiplicityRequest(7, 8))
                                        .retrieve()
                                        .bodyToMono(MathResponse.class)
                                        .doOnNext(it -> log.info(String.valueOf(it.getValue())));

        StepVerifier.create(mathResponseMono).expectNextCount(1).verifyComplete();
    }

    @DisplayName("Should set some custom header")
    @Test
    void shouldSetSomeCustomHeader() {
        var mathResponseMono = webClient.post()
                                        .uri("/reactive-math/router/multiply")
                                        //if producer type use body(), else bodyValue()
                                        .bodyValue(new MultiplicityRequest(7, 8))
                                        .headers(h -> h.set("someCustomKey", "someCustomValue"))
                                        .retrieve()
                                        .bodyToMono(MathResponse.class)
                                        .doOnNext(it -> log.info(String.valueOf(it.getValue())));

        StepVerifier.create(mathResponseMono).expectNextCount(1).verifyComplete();
    }

    @DisplayName("Should react on bad request exception")
    @Test
    void shouldReactOnBadRequestException() {
        var mathResponseMono = webClient.post()
                                        .uri("/reactive-math/router/multiply")
                                        //if producer type use body(), else bodyValue()
                                        .bodyValue(Integer.valueOf(10))
                                        .retrieve()
                                        .bodyToMono(MathResponse.class)
                                        .doOnNext(it -> log.info(String.valueOf(it.getValue())))
                                        .doOnError(it -> log.info(String.format("ERROR=>%s", it.getMessage())));

        StepVerifier.create(mathResponseMono).verifyError(WebClientResponseException.BadRequest.class);

    }

    @DisplayName("Should react on bad request exeption and retrieve additional information")
    @Test
    void shouldReactOnBadRequestExeptionAndRetrieveAdditionalInformation() {
        //exchange = retrieve + additional info about http status
        var mathResponseMono = webClient.post()
                                        .uri("/reactive-math/router/multiply")
                                        //if producer type use body(), else bodyValue()
                                        .bodyValue(Integer.valueOf(10))
                                        .exchangeToMono(this::exchange)
                                        //.bodyToMono(MathResponse.class)
                                        .doOnNext(it -> log.info(String.valueOf(it)))
                                        .doOnError(it -> log.info(String.format("ERROR=>%s", it.getMessage())));
        // it never will be an error - exchange() method changes the type - output will be normal object type
        StepVerifier.create(mathResponseMono).expectNextCount(1).verifyComplete();
    }

     private Mono<Object> exchange(ClientResponse resp) {
         if (resp.rawStatusCode()==400) {
             return resp.bodyToMono(String.class);
         }else {
             return resp.bodyToMono(MathResponse.class);
         }
    }

}