package com.vlad.kuzhyr.rideservice.utility.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PriceCalculator {

    private static final BigDecimal RATE_PER_KM = BigDecimal.valueOf(10);
    private static final BigDecimal METERS_IN_KM = BigDecimal.valueOf(1000);

    public BigDecimal calculatePrice(double distanceInMeters) {
        log.debug("Price calculator. Calculating price. Distance: {} meters", distanceInMeters);
        BigDecimal price =
            RATE_PER_KM.multiply(BigDecimal.valueOf(distanceInMeters)).divide(METERS_IN_KM, RoundingMode.HALF_EVEN);
        log.info("Price calculator. Calculated price. Distance: {} meters, Price: {}", distanceInMeters, price);
        return price;
    }
}