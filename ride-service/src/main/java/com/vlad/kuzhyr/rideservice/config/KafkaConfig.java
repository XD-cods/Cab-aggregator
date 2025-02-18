package com.vlad.kuzhyr.rideservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topic.ride-completed-topic}")
    private String rideCompletedTopic;

    @Value("${spring.kafka.topic.driver-busy-topic}")
    private String driverBusyTopic;

    @Bean
    public NewTopic rideCompletedTopic() {
        return TopicBuilder
            .name(rideCompletedTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic driverBusyTopic() {
        return TopicBuilder
            .name(driverBusyTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

}
