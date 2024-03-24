package com.Img2Tags.config;

import com.Img2Tags.dto.rabbitMQ.RabbitMQType;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    @Bean
    public Queue notificationQueue() {
        return new Queue(RabbitMQType.IMG2TAGS_CSV);
    }
}
