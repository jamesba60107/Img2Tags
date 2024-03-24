package com.Img2Tags.dto.rabbitMQ;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RabbitMQRequest extends RabbitMQBaseDto {

    private Integer imageCount;
    private String filePath;
    private List<String> imageNames;
}
