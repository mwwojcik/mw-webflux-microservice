package mw.webflux.microservices.math;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.annotation.processing.Generated;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MathResponse {

    private Instant created;
    private int value;

    public static MathResponse from(Instant created, int value) {
        return new MathResponse(created, value);
    }


}
