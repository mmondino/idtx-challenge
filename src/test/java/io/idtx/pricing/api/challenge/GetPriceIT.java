package io.idtx.pricing.api.challenge;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GetPriceIT {

    @Autowired
    private TestRestTemplate restTestClient;

    @ParameterizedTest
    @MethodSource("getTestDataForChallengeTest")
    void challengeTest(Map<String, String> testData) throws JSONException {

        String urlTemplate = "/api/v1/prices?brandId={brandId}&productId={productId}&pricingDate={pricingDate}";

        ResponseEntity<String> response = this.restTestClient.getForEntity(
                urlTemplate,
                String.class,
                testData.get("brandIdParamValue"),
                testData.get("productIdParamValue"),
                testData.get("pricingDateParamValue"));

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONObject responseBodyAsJsonObject = new JSONObject(response.getBody());
        Assertions.assertEquals(testData.get("expectedBrandId"), responseBodyAsJsonObject.getString("brandId"));
        Assertions.assertEquals(testData.get("expectedProductId"), responseBodyAsJsonObject.getString("productId"));
        Assertions.assertEquals(testData.get("expectedPriceListId"), responseBodyAsJsonObject.getString("priceListId"));
        Assertions.assertEquals(testData.get("expectedValidFrom"), responseBodyAsJsonObject.getString("validFrom"));
        Assertions.assertEquals(testData.get("expectedValidTo"), responseBodyAsJsonObject.getString("validTo"));
        Assertions.assertEquals(testData.get("expectedAmount"), responseBodyAsJsonObject.getString("amount"));
        Assertions.assertEquals(testData.get("expectedCurrency"), responseBodyAsJsonObject.getString("currency"));
    }

    private static Stream<Map<String, String>> getTestDataForChallengeTest() {

        // Test data for Test 1: "Petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)"
        Map<String, String> test1 = new HashMap();
        test1.put("brandIdParamValue", "1");
        test1.put("productIdParamValue", "35455");
        test1.put("pricingDateParamValue", "2020-06-14T10:00:00");
        test1.put("expectedBrandId", "1");
        test1.put("expectedProductId", "35455");
        test1.put("expectedPriceListId", "1");
        test1.put("expectedValidFrom", "2020-06-14T00:00:00");
        test1.put("expectedValidTo", "2020-12-31T23:59:59");
        test1.put("expectedAmount", "35.50");
        test1.put("expectedCurrency", "EUR");

        // Test data for Test 2: "Petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)"
        Map<String, String> test2 = new HashMap();
        test2.put("brandIdParamValue", "1");
        test2.put("productIdParamValue", "35455");
        test2.put("pricingDateParamValue", "2020-06-14T16:00:00");
        test2.put("expectedBrandId", "1");
        test2.put("expectedProductId", "35455");
        test2.put("expectedPriceListId", "2");
        test2.put("expectedValidFrom", "2020-06-14T15:00:00");
        test2.put("expectedValidTo", "2020-06-14T18:30:00");
        test2.put("expectedAmount", "25.45");
        test2.put("expectedCurrency", "EUR");

        // Test data for Test 3: "Petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)"
        Map<String, String> test3 = new HashMap();
        test3.put("brandIdParamValue", "1");
        test3.put("productIdParamValue", "35455");
        test3.put("pricingDateParamValue", "2020-06-14T21:00:00");
        test3.put("expectedBrandId", "1");
        test3.put("expectedProductId", "35455");
        test3.put("expectedPriceListId", "1");
        test3.put("expectedValidFrom", "2020-06-14T00:00:00");
        test3.put("expectedValidTo", "2020-12-31T23:59:59");
        test3.put("expectedAmount", "35.50");
        test3.put("expectedCurrency", "EUR");

        // Test data for Test 4: "Petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)"
        Map<String, String> test4 = new HashMap();
        test4.put("brandIdParamValue", "1");
        test4.put("productIdParamValue", "35455");
        test4.put("pricingDateParamValue", "2020-06-15T10:00:00");
        test4.put("expectedBrandId", "1");
        test4.put("expectedProductId", "35455");
        test4.put("expectedPriceListId", "3");
        test4.put("expectedValidFrom", "2020-06-15T00:00:00");
        test4.put("expectedValidTo", "2020-06-15T11:00:00");
        test4.put("expectedAmount", "30.50");
        test4.put("expectedCurrency", "EUR");

        // Test data for Test 5: "Petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)"
        Map<String, String> test5 = new HashMap();
        test5.put("brandIdParamValue", "1");
        test5.put("productIdParamValue", "35455");
        test5.put("pricingDateParamValue", "2020-06-16T21:00:00");
        test5.put("expectedBrandId", "1");
        test5.put("expectedProductId", "35455");
        test5.put("expectedPriceListId", "4");
        test5.put("expectedValidFrom", "2020-06-15T16:00:00");
        test5.put("expectedValidTo", "2020-12-31T23:59:59");
        test5.put("expectedAmount", "38.95");
        test5.put("expectedCurrency", "EUR");

        return Stream.of(test1, test2, test3, test4, test5);
    }
}