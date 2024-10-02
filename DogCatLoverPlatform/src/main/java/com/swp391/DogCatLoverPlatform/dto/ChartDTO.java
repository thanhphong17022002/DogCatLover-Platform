package com.swp391.DogCatLoverPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartDTO {


    private Date create_date;
    private java.sql.Date bookingDate;
    private Time bookingTime;
    private double total_price;
    private String paying_method;
    private boolean status;
    private BlogDTO blogDTO;
    private UserDTO userDTO;

    private String schedule;
    private String title;
    private String content;
    private String image;
    private String userName;
    private double price;

    private Date dateStart;
    private Date dateEnd;
    private int id_user;
    private int id_blog;
    private String emailUserCreate;
    private Boolean confirm;
    private String serviceCateName;

    private int blogCountByWeek;
    private int serviceCountByWeek;
    private int bookingCount;

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public String getCreate_date() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        return formatter.format(create_date);
    }
}
