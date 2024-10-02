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
public class UserNotificationDTO {
    private int id;
    private String userName;    //Người accepted
    private int userId;
    private String userImage;
    private Date createDate;
    private String message;
    private Boolean status;

    private RequestDTO request;
    private int requestId;
    private int currentUserId;
    private int requestUserId;
    private int idReceiver;
}
