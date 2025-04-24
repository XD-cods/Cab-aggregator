package com.vlad.kuzhyr.authservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topic.driver-create-topic}")
    private String driverCreateTopic;

    @Value("${spring.kafka.topic.passenger-create-topic}")
    private String passengerCreateTopic;

    @Bean
    public NewTopic driverCreateTopic() {
        return TopicBuilder
            .name(driverCreateTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic passengerCreateTopic() {
        return TopicBuilder
            .name(passengerCreateTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

}
