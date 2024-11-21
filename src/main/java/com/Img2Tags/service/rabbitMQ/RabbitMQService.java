package com.Img2Tags.service.rabbitMQ;

import com.Img2Tags.dto.rabbitMQ.RabbitMQResponse;
import com.Img2Tags.dto.rabbitMQ.RabbitMQType;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
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

    public boolean checkRabbitMQConnection(String queueName) {
        try {
            // 使用 RabbitTemplate 發送一條測試消息到空路由
            rabbitTemplate.execute(channel -> {
                // 試圖聲明一個不存在的隊列作為測試
                channel.queueDeclarePassive(queueName);
                return null;
            });
            return true; // 如果沒有異常，則連線成功
        } catch (AmqpException e) {
            // 捕獲異常，表示連線失敗
            e.printStackTrace();
            return false;
        }
    }
}
