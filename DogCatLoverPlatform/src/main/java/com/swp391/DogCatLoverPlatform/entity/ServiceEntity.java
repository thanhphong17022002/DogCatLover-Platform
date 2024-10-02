package com.swp391.DogCatLoverPlatform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Entity(name= "service")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "schedule")
    private String schedule;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_start")
    private java.sql.Date  date_start;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_end")
    private java.sql.Date  date_end;


    @OneToOne
    @JoinColumn(name = "id_blog") // Checked
    private BlogEntity blog_service;

    @ManyToOne
    @JoinColumn(name = "id_service_cate")
    private ServiceCategoryEntity service_category;
}
