package com.swp391.DogCatLoverPlatform.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swp391.DogCatLoverPlatform.config.CustomDoubleSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "otp")
    private String otp;

    @ManyToOne
    @JoinColumn(name="id_role")
    private RoleEntity roleEntity;

    @OneToMany(mappedBy = "userEntity")
    List<BlogEntity> listBlogs;

    @OneToMany(mappedBy = "userEntity_CommentEntity")
    List<CommentEntity> listComment_UserEntity ;

    @OneToMany(mappedBy = "userEntity_BookingEntity")
    List<BookingEntity> listBooking_UserEntity ;

    @OneToMany(mappedBy = "userEntity_Request")
    List<RequestEntity> listRequest_UserEntity ;

    @OneToMany(mappedBy = "userEntity_UserNotification")
    List<UserNotificationEntity> listNotification;

    @OneToMany(mappedBy = "userEntity")
    private List<InvoiceEntity> invoiceEntities;


    @JsonDeserialize(using = CustomDoubleSerializer.class)
    private Double accountBalance;


}
