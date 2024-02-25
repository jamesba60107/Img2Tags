package com.Img2Tags.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadApiResponseDTO extends Status {

    private String imageName;
    @JsonProperty
    private String upload_id;
}
