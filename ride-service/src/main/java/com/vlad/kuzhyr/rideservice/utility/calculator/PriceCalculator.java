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
        log.debug("calculatePrice: Entering method. Distance: {} meters", distanceInMeters);
        if (distanceInMeters <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal price =
            RATE_PER_KM.multiply(BigDecimal.valueOf(distanceInMeters)).divide(METERS_IN_KM, RoundingMode.HALF_EVEN);
        log.debug("calculatePrice: Calculated price. Distance: {} meters, Price: {}", distanceInMeters, price);
        return price;
    }

}
