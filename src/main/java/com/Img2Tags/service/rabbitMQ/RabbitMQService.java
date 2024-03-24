package com.Img2Tags.service.rabbitMQ;

import com.Img2Tags.dto.rabbitMQ.RabbitMQResponse;
import com.Img2Tags.dto.rabbitMQ.RabbitMQType;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    @Autowired
    private Gson gson;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendImg2TagCSVCompleteToQueue(RabbitMQResponse response) {
        String jsonMessage = gson.toJson(response);
        rabbitTemplate.convertAndSend(RabbitMQType.IMG2TAGS_CSV, jsonMessage);
    }
}
