package com.example.demo.Model.User;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    private String username;
    private String email;
    private String password;
    private String country;

}
