package com.Img2Tags.dto;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private Double confidence;
    private TagContent tag;
}
