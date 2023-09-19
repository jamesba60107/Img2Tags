package com.Img2Tags.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImageUploadApiResponseDTO {

    private Result result;
    private Status status;

    @Getter
    @Setter
    @ToString
    public static class Result {
        private String imageName;
        @JsonProperty
        private String upload_id;
    }

    @Getter
    @Setter
    @ToString
    public static class Status {
        private String text;
        private String type;
    }
}
