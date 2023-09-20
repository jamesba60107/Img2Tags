package com.Img2Tags.model;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "imagga")
// 自定義application.properties文件 與Imagga相關的設置。
public class ImaggaProperties {

    private String key;

    private String secret;

    private String authorization;

    private String apiUrl;
}