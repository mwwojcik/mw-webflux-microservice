package mw.webflux.microservices.multiparams;

import java.net.URI;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import mw.webflux.microservices.math.MathRouterFunctionConfiguration;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@EnableAutoConfiguration
@SpringBootTest(classes = {ParamController.class, MathRouterFunctionConfiguration.class}, webEnvironment =
    WebEnvironment.DEFINED_PORT)
@Slf4j
public class ParamControllerTest {

    @Autowired
    private WebClient webClient;

    private String queryString="http://localhost:9898/jobs/search?count={count}&page={page}";

    @DisplayName("Should test multiparams query request")
    @Test
    void shouldTestMultiparamsQueryRequest() {

        // given
        var uri = UriComponentsBuilder.fromUriString(queryString).build(10, 20);

        Map<String,Integer> params=Map.of("count",10,"page",20);
        var integerFlux = webClient.get()
                                   .uri(b->b.path("jobs/search").query("count={count}&page={page}").build(params))
                                   .retrieve()
                                   .bodyToFlux(Integer.class)
                                   .doOnNext(it -> log.info(String.valueOf(it)));
        StepVerifier.create(integerFlux).expectNextCount(2).verifyComplete();
     }
}
