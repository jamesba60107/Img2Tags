package com.Img2Tags;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class Img2TagsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Img2TagsApplication.class, args);
    }
}
