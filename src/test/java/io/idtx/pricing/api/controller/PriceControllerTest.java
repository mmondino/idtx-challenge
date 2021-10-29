package io.idtx.pricing.api.controller;

import io.idtx.pricing.api.application.problem.ProblemCode;
import io.idtx.pricing.api.application.problem.ProblemType;
import io.idtx.pricing.api.application.service.GetPriceRequestModel;
import io.idtx.pricing.api.application.service.GetPriceResponseModel;
import io.idtx.pricing.api.application.service.GetPriceService;
import io.idtx.pricing.api.domain.exception.PriceNotFoundException;
import io.idtx.pricing.api.domain.exception.TooManyPricesFoundException;
import io.idtx.pricing.api.ui.http.controller.PriceController;
import io.idtx.pricing.api.ui.http.problem.FieldErrorTypeEnum;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPriceService getPriceService;

    @Test
    void whenPriceRequestedMatchOnePrice_shouldReturnPrice() throws Exception {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        GetPriceRequestModel serviceInvocationRequest = GetPriceRequestModel.builder()
                .brandId(1L)
                .productId(2L)
                .pricingDate(LocalDateTime.parse("2021-01-01T01:00:00"))
                .build();

        GetPriceResponseModel serviceInvocationResponse = GetPriceResponseModel
                .builder()
                .validFrom(LocalDateTime.parse("2021-01-01T00:00:00"))
                .validTo(LocalDateTime.parse("2021-12-31T23:59:59"))
                .brandId(1L)
                .productId(2L)
                .priceListId(3L)
                .amount(new BigDecimal(4.5))
                .currency("EUR")
                .build();

        Mockito.when(getPriceService.invoke(serviceInvocationRequest)).thenReturn(serviceInvocationResponse);

        this.mockMvc
                .perform(get("/api/v1/prices")
                        .queryParam("brandId", serviceInvocationRequest.getBrandId().toString())
                        .queryParam("productId", serviceInvocationRequest.getProductId().toString())
                        .queryParam("pricingDate", serviceInvocationRequest.getPricingDate().format(dateTimeFormatter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validFrom", Matchers.is(serviceInvocationResponse.getValidFrom().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.validTo", Matchers.is(serviceInvocationResponse.getValidTo().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.brandId", Matchers.is(serviceInvocationResponse.getBrandId()), Long.class))
                .andExpect(jsonPath("$.productId", Matchers.is(serviceInvocationResponse.getProductId()), Long.class))
                .andExpect(jsonPath("$.priceListId", Matchers.is(serviceInvocationResponse.getPriceListId()), Long.class))
                .andExpect(jsonPath("$.amount", Matchers.is(serviceInvocationResponse.getAmount().toString())))
                .andExpect(jsonPath("$.currency", Matchers.is(serviceInvocationResponse.getCurrency())));
    }

    @Test
    void whenPriceRequestedDoesNotMatchAnyPrice_shouldReturnHttpsStatusNotFoundWithProblemDetail() throws Exception {

        String brandId = "1";
        String productId = "2";
        String pricingDate = "2021-01-01T01:00:00";

        Mockito.when(getPriceService.invoke(ArgumentMatchers.any())).thenThrow(PriceNotFoundException.class);

        this.mockMvc
                .perform(get("/api/v1/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("pricingDate", pricingDate))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.problemType", Matchers.is(ProblemType.RESOURCE_NOT_FOUND.name())))
                .andExpect(jsonPath("$.problemCode", Matchers.is(ProblemCode.PRICE_NOT_FOUND.getCode())));
    }

    @Test
    void whenPriceRequestedMatchMoreThanOnePrice_shouldReturnHttpsStatus422WithProblemDetail() throws Exception {

        String brandId = "1";
        String productId = "2";
        String pricingDate = "2021-01-01T01:00:00";

        Mockito.when(getPriceService.invoke(ArgumentMatchers.any())).thenThrow(TooManyPricesFoundException.class);

        this.mockMvc
                .perform(get("/api/v1/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("pricingDate", pricingDate))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.problemType", Matchers.is(ProblemType.BUSINESS_RULE_PROBLEM.name())))
                .andExpect(jsonPath("$.problemCode", Matchers.is(ProblemCode.TOO_MANY_PRICES_FOUND.getCode())));
    }

    @Test
    void whenExceptionIsThrown_shouldReturnHttpsStatus500WithProblemDetail() throws Exception {

        String brandId = "1";
        String productId = "2";
        String pricingDate = "2021-01-01T01:00:00";

        Mockito.when(getPriceService.invoke(ArgumentMatchers.any())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(get("/api/v1/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("pricingDate", pricingDate))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.problemType", Matchers.is(ProblemType.API_ERROR.name())));
    }

    @ParameterizedTest
    @CsvSource({
            ",2,2021-01-01T01:00:00",  // Null brandId
            "1,,2021-01-01T01:00:00",  // Null productId
            "1,2,",                    // Null pricingDate
            "A,2,2021-01-01T01:00:00", // Invalid brandId
            "1,A,2021-01-01T01:00:00", // Invalid productId
            "1,2,AAAA-01-01T01:00:00"  // Invalid pricingDate
    })
    void whenRequestIsInvalid_shouldReturnHttpStatus400(String brandId, String productId, String pricingDate) throws Exception {

        Mockito.when(getPriceService.invoke(ArgumentMatchers.any())).thenThrow(PriceNotFoundException.class);

        this.mockMvc
                .perform(get("/api/v1/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("pricingDate", pricingDate))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnConversionErrors() throws Exception {

        String brandId = "A";
        String productId = "B";
        String pricingDate = "C";

        Mockito.when(getPriceService.invoke(ArgumentMatchers.any())).thenThrow(PriceNotFoundException.class);

        this.mockMvc
                .perform(get("/api/v1/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("pricingDate", pricingDate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.problemType", Matchers.is(ProblemType.INVALID_REQUEST.name())))
                .andExpect(jsonPath("$.fieldErrors", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.fieldErrors[*].path", Matchers.containsInAnyOrder("brandId", "productId", "pricingDate")))
                .andExpect(jsonPath("$.fieldErrors[0].errorType", Matchers.is(FieldErrorTypeEnum.CONVERSION_ERROR.name())))
                .andExpect(jsonPath("$.fieldErrors[1].errorType", Matchers.is(FieldErrorTypeEnum.CONVERSION_ERROR.name())))
                .andExpect(jsonPath("$.fieldErrors[2].errorType", Matchers.is(FieldErrorTypeEnum.CONVERSION_ERROR.name())));
    }

    @Test
    void shouldReturnValidationErrors() throws Exception {

        String brandId = "";
        String productId = "";
        String pricingDate = "";

        Mockito.when(getPriceService.invoke(ArgumentMatchers.any())).thenThrow(PriceNotFoundException.class);

        this.mockMvc
                .perform(get("/api/v1/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("pricingDate", pricingDate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.problemType", Matchers.is(ProblemType.INVALID_REQUEST.name())))
                .andExpect(jsonPath("$.fieldErrors", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.fieldErrors[*].path", Matchers.containsInAnyOrder("brandId", "productId", "pricingDate")))
                .andExpect(jsonPath("$.fieldErrors[0].errorType", Matchers.is(FieldErrorTypeEnum.VALIDATION_ERROR.name())))
                .andExpect(jsonPath("$.fieldErrors[1].errorType", Matchers.is(FieldErrorTypeEnum.VALIDATION_ERROR.name())))
                .andExpect(jsonPath("$.fieldErrors[2].errorType", Matchers.is(FieldErrorTypeEnum.VALIDATION_ERROR.name())));
    }
}