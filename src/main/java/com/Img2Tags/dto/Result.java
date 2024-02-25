package com.Img2Tags.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Result {
    private String imageName;
    private List<Tag> tags;
}