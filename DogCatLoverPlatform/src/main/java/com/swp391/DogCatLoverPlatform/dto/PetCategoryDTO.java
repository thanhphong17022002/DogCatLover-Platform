package com.swp391.DogCatLoverPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetCategoryDTO {

    private int id;
    private String name;
    private String breed;
    private int age;
    private String color;
    private double weight;

}
