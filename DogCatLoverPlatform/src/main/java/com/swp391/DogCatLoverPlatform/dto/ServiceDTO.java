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
public class ServiceDTO {
    private int id;
    private String schedule;
    private String title;
    private String content;
    private String image;
    private Date createDate;
    private String userName;
    private double price;
    private Boolean status;
    private Date dateStart;
    private Date dateEnd;
    private int id_user;
    private int id_blog;
    private String emailUserCreate;
    private Boolean confirm;
    private String serviceCateName;

    private BlogDTO blog;

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }
}
