package com.Img2Tags.dto.rabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMQBaseDto {

    private String requestId;
    private String userId;
}
