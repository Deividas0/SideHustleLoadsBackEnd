package com.example.demo.Model.User;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    private String username;
    private String country;
    private String whatsapp;
    private String viber;
    private String email;
    private String password;
    private String registrationDate;
    private int totalListingsCreated;

}
