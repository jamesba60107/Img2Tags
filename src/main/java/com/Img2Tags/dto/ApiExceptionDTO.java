package com.Img2Tags.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@AllArgsConstructor
@ToString
@Getter
public class ApiExceptionDTO {

    private int errorCode;
    private String message;
    private String timeStamp;
}