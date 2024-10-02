package com.swp391.DogCatLoverPlatform.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swp391.DogCatLoverPlatform.config.CustomDoubleSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "disposit_history")
public class DispositHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonDeserialize(using = CustomDoubleSerializer.class)
    private Double totalAmount;

    private Date createdDate;

    private Time createdTime;

    private String requestId;

    private String orderId;

    private String token;

    private String paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
