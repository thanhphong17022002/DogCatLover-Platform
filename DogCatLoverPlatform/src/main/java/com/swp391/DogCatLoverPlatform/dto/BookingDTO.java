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
public class BookingDTO {
    private int id;
    private Date create_date;
    private java.sql.Date bookingDate;
    private Time bookingTime;
    private double total_price;
    private String paying_method;
    private boolean status;
    private BlogDTO blogDTO;
    private UserDTO userDTO;
}
