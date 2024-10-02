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

public class InvoiceDTO {

    private int id;
    private Date invoice_date;
    private double total_amount;

    private String email;
    private String title;
    private String paying_method;

    private String userName;

    private int idBlog;
    private int idUser;
}
