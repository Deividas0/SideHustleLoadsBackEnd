package com.example.demo.Service;

import com.example.demo.Model.User.UserProfileDTO;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    public boolean checkIfUsernameExists(String username) throws SQLException {
        return userRepository.checkIfUsernameExists(username);
    }

    public boolean checkIfEmailExists(String email) throws SQLException {
        return userRepository.checkIfEmailExists(email);
    }

    public void registration(String username, String email, String country, String password) throws SQLException {
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.registration(username, email, country, hashedPassword);
    }

    public int login(String username, String password) throws SQLException {

        String storedPassword = userRepository.findPasswordByUsername(username);

        if (storedPassword != null && passwordEncoder.matches(password, storedPassword)) {
            return userRepository.getUserId(username);
        }
        return 0;
    }


    public void totalListingsCreatedIncrement(int id) throws SQLException {
        userRepository.totalListingsCreatedIncrement(id);
    }

    public UserProfileDTO getUserProfileById(int id) throws SQLException {
        return userRepository.getUserProfileById(id);
    }

    public void updateUserProfile(int id, UserProfileDTO updatedProfile) throws SQLException {
        userRepository.updateUserProfile(id, updatedProfile);
    }

    public void updateUserBalanceById(int id, int balance) throws SQLException {
        userRepository.updateUserBalanceById(id,balance);
    }

    public int getUserBalanceById(int id) throws SQLException {
        return userRepository.getUserBalanceById(id);
    }

    public String getUserEmailById(int id) throws SQLException {
        return userRepository.getUserEmailById(id);
    }

    public String getUserUsernameById(int id) throws SQLException {
        return userRepository.getUserUsernameById(id);
    }

    public void updateUserStatusToVipById(int id) throws SQLException {
        userRepository.updateUserStatusToVipById(id);
    }

    public String getUserStatusById(int id) throws SQLException {
        return userRepository.getUserStatusById(id);
    }
}

