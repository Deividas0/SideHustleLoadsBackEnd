package com.example.demo.Model.User;

public class UserMapper {

    public static User toUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword());
        return user;
    }

    public static UserLoginDTO toLoginDTO(User user) {
        return new UserLoginDTO(
                user.getEmail(),
                user.getPassword()
        );
    }

    public static UserProfileDTO toProfileDTO(User user) {
        return new UserProfileDTO(
                user.getUsername(),
                user.getRegistrationDate(),
                user.getTotalListingsCreated()
        );
    }
}
