package com.vlad.kuzhyr.rideservice.utility.fallback.factory;

import com.vlad.kuzhyr.rideservice.utility.client.PassengerFeignClient;
import com.vlad.kuzhyr.rideservice.utility.fallback.PassengerFeignClientFallback;
import org.springframework.cloud.openfeign.FallbackFactory;

public class PassengerFeignClientFallbackFactory implements FallbackFactory<PassengerFeignClient> {

    @Override
    public PassengerFeignClient create(Throwable cause) {
        return new PassengerFeignClientFallback(cause);
    }

}
