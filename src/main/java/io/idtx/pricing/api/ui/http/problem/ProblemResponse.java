package io.idtx.pricing.api.ui.http.problem;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ProblemResponse {

    private Instant timestamp;
    private String problemType;
    private String problemCode;
    private String message;
}
