package com.Img2Tags.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FileUploadConfig {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String getMaxFileSize;

    @Value("${spring.servlet.multipart.location}")
    private String UPLOAD_DIR;

}
