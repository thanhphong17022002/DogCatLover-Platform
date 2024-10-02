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
@Entity
@Table(name = "invoice")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "invoice_date")
    private Date invoice_date;

    @Column(name = "total_amount")
    private double total_amount;

    @Column(name = "paying_method")
    private String paying_method;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity; // Thay UserEntity bằng tên Entity tương ứng cho bảng users

    @ManyToOne
    @JoinColumn(name = "id_blog")
    private BlogEntity blogEntity; // Thay BlogEntity bằng tên Entity tương ứng cho bảng blog


}
