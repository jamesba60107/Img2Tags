package com.Img2Tags.controller;

import com.Img2Tags.dto.rabbitMQ.RabbitMQRequest;
import com.Img2Tags.service.rabbitMQ.RabbitMQListener;
import com.Img2Tags.service.rabbitMQ.RabbitMQService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
public class RabbitMQController {

    private final RabbitMQService rabbitMQService;
    private final RabbitMQListener rabbitMQListener;

    public RabbitMQController(RabbitMQService rabbitMQService,
                              RabbitMQListener rabbitMQListener) {
        this.rabbitMQService = rabbitMQService;
        this.rabbitMQListener = rabbitMQListener;
    }

    @Operation(summary = "檢查rabbitMQ queue有無正常連線", description = "參數: IDJ, img2tagscsv.queue")
    @GetMapping("/checkRabbitMQ")
    public String checkRabbitMQ(@RequestParam String queueName) {
        boolean isConnected = rabbitMQService.checkRabbitMQConnection(queueName);
        return isConnected ? "RabbitMQ is connected!" : "RabbitMQ connection failed!";
    }

    @Operation(summary = "呼叫rabbitMQ IDJ Listener方法")
    @PostMapping("/processMessage/IDJ")
    public String processMessageIDJ(RabbitMQRequest request) {
        try {
            String jsonMessage = new ObjectMapper().writeValueAsString(request);
            rabbitMQListener.receiveMessage(jsonMessage);
            return "OK";
        } catch (Exception e) {
            return "Fail";
        }
    }
}
