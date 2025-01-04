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
}
