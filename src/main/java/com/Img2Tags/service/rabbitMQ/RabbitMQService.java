package com.Img2Tags.service.rabbitMQ;

import com.Img2Tags.dto.rabbitMQ.RabbitMQResponse;
import com.Img2Tags.dto.rabbitMQ.RabbitMQType;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQService {

    @Autowired
    private Gson gson;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendImg2TagCSVCompleteToQueue(RabbitMQResponse response) {
        log.info("發送message至RabbitMQ: {}", response);
        String jsonMessage = gson.toJson(response);
        rabbitTemplate.convertAndSend(RabbitMQType.IMG2TAGSCSV, jsonMessage);
    }
}
