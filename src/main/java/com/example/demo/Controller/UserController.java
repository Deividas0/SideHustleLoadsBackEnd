package com.example.demo.Controller;

import com.example.demo.Emails.EmailsService;
import com.example.demo.Model.User.UserLoginDTO;
import com.example.demo.Model.User.UserProfileDTO;
import com.example.demo.Model.User.UserRegistrationDTO;
import com.example.demo.Service.UserService;
import com.example.demo.Utility.JwtToken.JwtDecoder;
import com.example.demo.Utility.JwtToken.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, allowCredentials = "true")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailsService emailsService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private JwtDecoder jwtDecoder;


    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody UserRegistrationDTO user) throws SQLException {
        if (userService.checkIfUsernameExists(user.getUsername())) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (userService.checkIfEmailExists(user.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        } else {
            userService.registration(user.getUsername(), user.getEmail(), user.getCountry(), user.getPassword());
            emailsService.registrationEmail(user.getEmail(), user.getUsername());
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

    @GetMapping("/profile")
    public UserProfileDTO getUserProfileDTO(@RequestHeader("Authorization") String authorizationHeader) throws SQLException {
        int id = jwtDecoder.decodeUserIdFromToken(authorizationHeader);
        return userService.getUserProfileById(id);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserProfileDTO updatedProfile
    ) throws SQLException {
        int id = jwtDecoder.decodeUserIdFromToken(authorizationHeader);
        userService.updateUserProfile(id, updatedProfile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getUserBalance(
            @RequestHeader("Authorization") String authorizationHeader) throws SQLException {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int id = jwtDecoder.decodeUserIdFromToken(authorizationHeader);
        int balance = userService.getUserBalanceById(id);

        return ResponseEntity.ok(Map.of("balance", balance));
    }

    @PostMapping("/vip")
    public ResponseEntity<Map<String, Object>> purchaseVip(
            @RequestBody Map<String, Integer> request,
            @RequestHeader("Authorization") String authorizationHeader) throws SQLException {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int id = jwtDecoder.decodeUserIdFromToken(authorizationHeader);

        String currentStatus = userService.getUserStatusById(id);
        if ("VIP".equalsIgnoreCase(currentStatus)) {
            return ResponseEntity.ok(Map.of("message", "You are already a VIP user."));
        }

        int amount = request.get("amount");

        if (amount != 4) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid amount"));
        }

        // Check balance
        int currentBalance = userService.getUserBalanceById(id);
        if (currentBalance < amount) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient balance"));
        }

        userService.updateUserBalanceById(id, -amount);
        userService.updateUserStatusToVipById(id);

        return ResponseEntity.ok(Map.of("message", "VIP status purchased successfully"));
    }



}
