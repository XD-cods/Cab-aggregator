package com.vlad.kuzhyr.rideservice.service.impl;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PriceService {

    private static final BigDecimal RATE_PER_KM = BigDecimal.valueOf(10);

    public BigDecimal calculatePrice(double distance) {
        return RATE_PER_KM.multiply(BigDecimal.valueOf(distance / 1000));
    }

}
