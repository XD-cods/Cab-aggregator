package com.vlad.kuzhyr.rideservice.utility.fallback.factory;

import com.vlad.kuzhyr.rideservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.rideservice.utility.fallback.DriverFeignClientFallback;
import org.springframework.cloud.openfeign.FallbackFactory;

public class DriverFeignClientFallbackFactory implements FallbackFactory<DriverFeignClient> {

    @Override
    public DriverFeignClient create(Throwable cause) {
        return new DriverFeignClientFallback(cause);
    }

}
