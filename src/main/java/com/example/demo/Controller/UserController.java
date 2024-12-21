package com.example.demo.Controller;

import com.example.demo.Emails.EmailsService;
import com.example.demo.Model.User.UserRegistrationDTO;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailsService emailsService;


    @PostMapping("/registration")
    private ResponseEntity<String> registration(@RequestBody UserRegistrationDTO user) throws SQLException {
        if (userService.checkIfUsernameExists(user.getUsername())) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (userService.checkIfEmailExists(user.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        } else {
            userService.registration(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword());
            emailsService.registrationEmail(user.getEmail(),
                    user.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
