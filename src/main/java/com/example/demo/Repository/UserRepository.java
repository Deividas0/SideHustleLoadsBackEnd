package com.example.demo.Repository;

import com.example.demo.Model.Listing.Listing;
import com.example.demo.Model.User.UserProfileDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Repository
public class UserRepository {

    @Value("${SQLURL}")
    private String SQLURL;

    @Value("${SQLUSERNAME}")
    private String SQLUSERNAME;

    @Value("${SQLPASSWORD}")
    private String SQLPASSWORD;

    private Connection _connection;

    public void sqlConnection() throws SQLException {
        _connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD);
    }

    public boolean checkIfUsernameExists(String username) throws SQLException {
        final String sql = "SELECT username FROM user where username = ?";

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean checkIfEmailExists(String email) throws SQLException {
        final String sql = "SELECT email FROM user where email = ?";

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void registration(String username, String email, String country, String password) throws SQLException {
        final String sql = "INSERT into user (username, email, country, password) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, country);
            preparedStatement.setString(4, password);

            preparedStatement.executeUpdate();
        }
    }

    public String findPasswordByUsername(String username) throws SQLException {
        final String sql = "SELECT password FROM user WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            // If the username is found, return the hashed password
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        }
        // If username is not found, return null
        return null;
    }

    public int getUserId(String username) throws SQLException {
        final String sql = "SELECT id FROM user WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }

        // If username is not found, return 0
        return 0;
    }

    public String getUserCountry(String username) throws SQLException {
        final String sql = "SELECT country FROM user WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("country");
            }
        }

        // If username is not found, return 0
        return null;
    }

    public void totalListingsCreatedIncrement(int id) throws SQLException {
        final String sql = "UPDATE user SET total_listings_created = total_listings_created + 1 WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }

    public UserProfileDTO getUserProfileById(int id) throws SQLException {
        final String sql = "SELECT username, country, whatsapp, viber, registration_date, total_listings_created FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            UserProfileDTO userProfileDTO = new UserProfileDTO();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    userProfileDTO.setUsername(resultSet.getString("username"));
                    userProfileDTO.setCountry(resultSet.getString("country"));
                    userProfileDTO.setWhatsapp(resultSet.getString("whatsapp"));
                    userProfileDTO.setViber(resultSet.getString("viber"));

                    Timestamp timestamp = resultSet.getTimestamp("registration_date");
                    LocalDateTime registrationDate = timestamp.toLocalDateTime();
                    String formattedDate = registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    userProfileDTO.setRegistrationDate(formattedDate);

                    userProfileDTO.setTotalListingsCreated(resultSet.getInt("total_listings_created"));
                }
            }
            return userProfileDTO;
        }
    }
}
