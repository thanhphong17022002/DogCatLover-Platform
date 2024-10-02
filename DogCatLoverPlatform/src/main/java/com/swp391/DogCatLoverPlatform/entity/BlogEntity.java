package com.swp391.DogCatLoverPlatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name= "blog")
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private double  price;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "confirm")
    private Boolean confirm;

    @Column(name = "reason")
    private String reason;


    @ManyToOne
    @JoinColumn(name="id_user_created")
    @JsonIgnore
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name="id_blog_type")
    @JsonIgnore
    private BlogTypeEntity blogTypeEntity;

    @ManyToOne
    @JoinColumn(name="id_pet_type")
    @JsonIgnore
    private PetTypeEntity petTypeEntity;


    @OneToMany(mappedBy = "blogEntity_BookingEntity")
    @JsonIgnore
    List<BookingEntity> listBooking_BlogEntity;

    @OneToMany(mappedBy = "blogEntity_CommentEntity")
    @JsonIgnore
    List<CommentEntity> listComment_BlogEntity ;


    @OneToMany(mappedBy = "blogEntity_Request")
    List<RequestEntity> listRequest_BlogEntity ;

    @OneToOne
    @JoinColumn(name = "id_pet_category")
    private PetCategoryEntity petCategoryEntity;

    @OneToMany(mappedBy = "blogEntity")
    private List<InvoiceEntity> invoices;






}
