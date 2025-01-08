package com.example.demo.Model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private String username;
    private String country;
    private String whatsapp;
    private String viber;
    private String registrationDate;
    private int totalListingsCreated;
    private int balance;
    private String status;

    public UserProfileDTO(String username, String country, String whatsapp, String viber, String registrationDate, int totalListingsCreated) {
        this.username = username;
        this.country = country;
        this.whatsapp = whatsapp;
        this.viber = viber;
        this.registrationDate = registrationDate;
        this.totalListingsCreated = totalListingsCreated;
    }
}
