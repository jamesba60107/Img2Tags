package com.Img2Tags.dto.rabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMQResponse {

    private String requestId;
    private String language;
    private String userId;
    private Integer successCount;
    private Integer failCount;
    private String csvPath;
    private String csvName;
}
