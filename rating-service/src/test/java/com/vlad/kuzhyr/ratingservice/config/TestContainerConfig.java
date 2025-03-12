package com.vlad.kuzhyr.ratingservice.config;

import com.vlad.kuzhyr.ratingservice.constant.TestContainerConstant;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainerConfig {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(TestContainerConstant.POSTGRES_IMAGE)
        .withDatabaseName(TestContainerConstant.POSTGRES_DATABASE_NAME)
        .withUsername(TestContainerConstant.POSTGRES_USERNAME)
        .withPassword(TestContainerConstant.POSTGRES_PASSWORD);

}
