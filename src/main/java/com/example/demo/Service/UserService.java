package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean checkIfUsernameExists(String username) throws SQLException {
        return userRepository.checkIfUsernameExists(username);
    }

    public boolean checkIfEmailExists(String email) throws SQLException {
        return userRepository.checkIfEmailExists(email);
    }

    public void registration(String username, String email,  String country, String password) throws SQLException {
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.registration(username, email, country, hashedPassword);
    }

    public int login(String username, String password) throws SQLException {

        String storedPassword = userRepository.findPasswordByUsername(username);

        if (storedPassword != null && passwordEncoder.matches(password, storedPassword)) {
            return userRepository.getUserId(username);
        }
        return 0;  // Invalid username or password
    }

    public String getUserCountry(String username) throws SQLException {
        return userRepository.getUserCountry(username);
    }

    public void totalListingsCreatedIncrement(int id) throws SQLException {
        userRepository.totalListingsCreatedIncrement(id);
    }

}
