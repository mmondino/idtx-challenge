package io.idtx.pricing.api.ui.http.problem;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class InvalidRequestResponse {

    private Instant timestamp;
    private String problemType;
    private String problemCode;
    private String message;

    private List<String> globalErrors;
    private List<FieldError> fieldErrors;
}
