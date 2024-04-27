package com.Img2Tags.config;

import com.Img2Tags.dto.rabbitMQ.RabbitMQType;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@Configuration
//@EnableRabbit
public class RabbitMQConfig {

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("192.168.0.130");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("jamesba");
//        connectionFactory.setPassword("Imagedj89684152");
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        return new RabbitTemplate(connectionFactory);
//    }

    @Bean
    public Queue Img2TagsCSVQueue() {

        return new Queue(RabbitMQType.IMG2TAGSCSV, true);
    }
}
