package com.swp391.DogCatLoverPlatform.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StatisticInvoiceDTO {

    private Date dateInvoice;
    private Long numberInvoice;
}
