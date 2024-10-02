package com.swp391.DogCatLoverPlatform.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private int id;
    private Date createDate;
    private String userName;
    private String userId;
    private String userImage;
    private String blogTitle;
    private String blogTypeName;
    private String blogPetType;
    private int blogId;
    private Boolean status;


}
