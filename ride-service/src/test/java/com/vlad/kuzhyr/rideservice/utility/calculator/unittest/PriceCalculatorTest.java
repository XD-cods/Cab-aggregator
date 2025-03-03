package com.vlad.kuzhyr.rideservice.utility.calculator.unittest;

import com.vlad.kuzhyr.rideservice.utility.calculator.PriceCalculator;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PriceCalculatorTest {

    private final PriceCalculator priceCalculator = new PriceCalculator();

    @Test
    void calculatePrice_shouldReturnCorrectPrice() {
        double distanceInMeters = 1000.0;
        BigDecimal expectedPrice = BigDecimal.valueOf(10.0);

        BigDecimal result = priceCalculator.calculatePrice(distanceInMeters);

        assertEquals(expectedPrice, result);
    }

    @Test
    void calculatePrice_shouldReturnZero() {
        double distanceInMeters = 0;
        BigDecimal expectedPrice = BigDecimal.valueOf(0);

        BigDecimal result = priceCalculator.calculatePrice(distanceInMeters);

        assertEquals(expectedPrice, result);
    }
}
