package com.swp391.DogCatLoverPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogUpdateDTO {

    private String title;
    private String content;
    private double price;
    private String blogTypeName;
    private Boolean confirm;
    private String reason;

}
