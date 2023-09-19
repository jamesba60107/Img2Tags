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

    @Getter
    @Setter
    @ToString
    public static class Result {
        private String imageName;
        private List<Tag> tags;
    }

    @Getter
    @Setter
    @ToString
    public static class Tag {
        private Double confidence;
        private TagContent tag;
    }

    @Getter
    @Setter
    @ToString
    public static class TagContent {
        private String en;
    }

    @Getter
    @Setter
    @ToString
    public static class Status {
        private String text;
        private String type;
    }
}
