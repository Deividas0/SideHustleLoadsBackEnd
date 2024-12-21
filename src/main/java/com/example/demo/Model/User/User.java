package com.example.demo.Model.User;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    private String username;
    private String email;
    private String password;
    private Timestamp registrationDate;
    private int totalListingsCreated;

}
