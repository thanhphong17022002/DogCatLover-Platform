package com.swp391.DogCatLoverPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String userName;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String image;
    private String description;
    private int id_role;
    private String roleDTO;

    private int totalBlogs;

    private Double balance;

}
