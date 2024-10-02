package com.swp391.DogCatLoverPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {


    private int id;
    private String title;
    private String content;
    private String image;
    private Date createDate;
    private double price;
    private boolean status;
    private boolean pet_type;
    private Boolean confirm;
    private String reason;
    private String schedule;


    private UserDTO userDTO;
    private String userName;
    private int userId;
    private String emailUserCreate;

    private BlogTypeDTO blogTypeDTO;
    private String blogTypeName;
    private int blogTypeId;

    private PetTypeDTO petTypeDTO;
    private int petTypeId;
    private String petType;

    private PetCategoryDTO petCategory; // Change the property name to 'petCategory'

}
