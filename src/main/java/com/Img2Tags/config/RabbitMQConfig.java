package com.Img2Tags.config;

import com.Img2Tags.dto.rabbitMQ.RabbitMQType;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue Img2TagsCSVQueue() {

        return new Queue(RabbitMQType.IMG2TAGSCSV, true);
    }
}
