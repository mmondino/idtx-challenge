package io.idtx.pricing.api.ui.http.api;

import io.idtx.pricing.api.application.service.GetPriceRequestModel;
import io.idtx.pricing.api.application.service.GetPriceResponseModel;
import io.idtx.pricing.api.domain.exception.PriceNotFoundException;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.ui.http.problem.InvalidRequestResponse;
import io.idtx.pricing.api.ui.http.problem.ProblemResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

public interface PriceApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = GetPriceResponseModel.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = InvalidRequestResponse.class))),
            @ApiResponse(responseCode = "404", description = "Price not found", content = @Content(schema = @Schema(implementation = ProblemResponse.class))),
            @ApiResponse(responseCode = "422", description = "Too many prices found", content = @Content(schema = @Schema(implementation = ProblemResponse.class))),
    })
    @GetMapping(value = "/api/v1/prices", produces = { "application/json" })
    GetPriceResponseModel getPrice(@Parameter(description = "Get product price", required = true) @Valid GetPriceRequestModel request) throws PriceNotFoundException, TooManyPricesFoundException;
}
