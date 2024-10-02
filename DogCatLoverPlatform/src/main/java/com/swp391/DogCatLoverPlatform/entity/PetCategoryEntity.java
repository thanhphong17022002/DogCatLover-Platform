package com.swp391.DogCatLoverPlatform.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name="pet_category")
public class PetCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;


    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private int age;

    @Column(name = "color")
    private String color;

    @Column(name = "weight")
    private double weight;

    @OneToOne(mappedBy = "petCategoryEntity")
    private BlogEntity blogEntity;


}