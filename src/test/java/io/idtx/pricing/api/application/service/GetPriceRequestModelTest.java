package io.idtx.pricing.api.application.service;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GetPriceRequestModelTest {

    @Test
    void testEqualsContract() {
        EqualsVerifier.simple().forClass(GetPriceRequestModel.class).verify();
    }
}