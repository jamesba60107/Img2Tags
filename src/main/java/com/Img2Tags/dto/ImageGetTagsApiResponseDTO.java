package com.Img2Tags.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ImageGetTagsApiResponseDTO {

    private Result result;
    private Status status;
}
