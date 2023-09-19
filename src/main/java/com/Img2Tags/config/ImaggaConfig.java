package com.Img2Tags.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Configuration
public class ImaggaConfig {


    @Value("${imagga.key}")
    private String key;

    @Value("${imagga.secret}")
    private String secret;

    @Value("${imagga.authorization}")
    private String authorization;

    @Value("${imagga.apiUrl}")
    private String apiUrl;

    @Bean
    public String getBasicAuth() {
        String credentialsToEncode = key + ":" + secret;
        return Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));
    }

}
