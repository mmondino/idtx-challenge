package io.idtx.pricing.api.ui.http.controller;

import io.idtx.pricing.api.application.service.GetPriceRequestModel;
import io.idtx.pricing.api.application.service.GetPriceResponseModel;
import io.idtx.pricing.api.application.service.GetPriceService;
import io.idtx.pricing.api.domain.exception.PriceNotFoundException;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.ui.http.api.PriceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/api/v1/prices", produces = MediaType.APPLICATION_JSON_VALUE)
public class PriceController implements PriceApi {

    private final GetPriceService getPriceService;

    @Autowired
    public PriceController(GetPriceService getPriceService) {
        this.getPriceService = getPriceService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GetPriceResponseModel getPrice(@Valid GetPriceRequestModel request) throws PriceNotFoundException, TooManyPricesFoundException {
        return this.getPriceService.invoke(request);
    }
}
