package com.example.demo.Controller;

import com.example.demo.Emails.EmailsService;
import com.example.demo.Model.User.UserLoginDTO;
import com.example.demo.Model.User.UserRegistrationDTO;
import com.example.demo.Service.UserService;
import com.example.demo.Utility.JwtToken.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailsService emailsService;

    @Autowired
    private JwtGenerator jwtGenerator;


    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody UserRegistrationDTO user) throws SQLException {
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO userLoginDTO) throws SQLException {
        int id = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        Map<String, String> response = new HashMap<>();

        if (id == 0) {
            response.put("error", "Invalid username or password");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("token", jwtGenerator.generateJwt(userLoginDTO.getUsername(), id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
