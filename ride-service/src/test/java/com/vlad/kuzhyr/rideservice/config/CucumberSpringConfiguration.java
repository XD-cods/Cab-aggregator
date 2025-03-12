package com.vlad.kuzhyr.rideservice.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EmbeddedKafka
@AutoConfigureWireMock(port = 9090)
@CucumberContextConfiguration
@ImportTestcontainers(TestContainerConfig.class)
public class CucumberSpringConfiguration {
}
