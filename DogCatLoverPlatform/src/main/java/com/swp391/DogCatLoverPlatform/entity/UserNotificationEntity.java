package com.swp391.DogCatLoverPlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="users_notification")
public class UserNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="message")
    private String message;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name="id_receiver")
    private int idReceiver;

    @Column(name="status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name="id_sender")
    private UserEntity userEntity_UserNotification;

    @ManyToOne
    @JoinColumn(name="id_request")
    private RequestEntity requestEntity;
}
