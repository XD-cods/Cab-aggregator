package com.vlad.kuzhyr.rideservice.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PriceService {

    private static final BigDecimal RATE_PER_KM = BigDecimal.valueOf(10);
    private static final BigDecimal METERS_IN_KM = BigDecimal.valueOf(1000);

    public BigDecimal calculatePrice(double distanceInMeters) {
        return RATE_PER_KM.multiply(BigDecimal.valueOf(distanceInMeters)).divide(METERS_IN_KM, RoundingMode.HALF_EVEN);
    }
}

