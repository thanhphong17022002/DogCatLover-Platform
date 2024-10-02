package com.swp391.DogCatLoverPlatform.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private String description;
    private int id_user;
    private int id_blog;
    private String userName;
    private String userImage;
    private Date createDate;
    private String blogTitle;
    private int id;
//    private int rating;

}
