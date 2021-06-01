package mw.webflux.microservices.math;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MultiplicityRequest {
    private int first;
    private int second;
}
