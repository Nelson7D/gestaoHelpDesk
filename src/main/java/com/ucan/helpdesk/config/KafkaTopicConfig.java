package com.ucan.helpdesk.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ticketCriado() {
        return TopicBuilder.name("ticket-criado")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ticketAtualizado() {
        return TopicBuilder.name("ticket-atualizado")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ticketFechado() {
        return TopicBuilder.name("ticket-fechado")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic slaViolado() {
        return TopicBuilder.name("sla-violado")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic logEvento() {
        return TopicBuilder.name("log-evento")
                .partitions(3)
                .replicas(1)
                .build();
    }
}