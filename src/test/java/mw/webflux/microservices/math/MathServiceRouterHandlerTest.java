package mw.webflux.microservices.math;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@SpringBootTest(classes = MathRouterFunctionConfiguration.class)
@Import({MathRouterFunctionConfiguration.class})
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
}