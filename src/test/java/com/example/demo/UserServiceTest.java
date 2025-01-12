package com.example.demo;

import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = new UserService();
        userService.userRepository = userRepository;
        userService.passwordEncoder = passwordEncoder;
    }

    @Test
    void testRegistration() throws SQLException {
        String username = "testUsername";
        String email = "test@gmail.com";
        String country = "Lithuania";
        String password = "password123";
        String hashedPassword = "hashedPassword123";

        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);

        userService.registration(username, email, country, password);

        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).registration(username, email, country, hashedPassword);
    }
}
