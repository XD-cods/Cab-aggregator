package com.vlad.kuzhyr.rideservice.constant;

import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WireMockStubs {

    public static void mockDriverService() {
        stubFor(
            WireMock.get(WireMock.urlPathMatching("/api/v1/drivers/.*"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                            "id": 1,
                            "first_name": "John",
                            "last_name": "Doe",
                            "email": "john.doe@example.com",
                            "gender": "MALE",
                            "phone": "+375335184521",
                            "car_ids": [1, 2],
                            "is_enabled": true,
                            "is_busy": false
                        }
                        """))
        );
    }

    public static void mockPassengerService() {
        stubFor(
            WireMock.get(WireMock.urlPathMatching("/api/v1/passengers/.*"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                            "id": 1,
                            "first_name": "Jane",
                            "last_name": "Doe",
                            "email": "jane.doe@example.com",
                            "phone": "+375295162318",
                            "is_enabled": true,
                            "is_busy": false
                        }
                        """))
        );
    }

    public static void mockMapboxGeocode() {
        stubFor(
            WireMock.get(WireMock.urlPathMatching("/search/geocode/v6/forward"))
                .withQueryParam("q", WireMock.matching(".*"))
                .withQueryParam("access_token", WireMock.matching(".*"))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                            "features": [
                                {
                                    "geometry": {
                                        "coordinates": [12.34, 56.78]
                                    }
                                }
                            ]
                        }
                        """))

        );
    }

}
