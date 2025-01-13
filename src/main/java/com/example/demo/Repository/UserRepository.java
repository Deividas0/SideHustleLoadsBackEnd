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

            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        }
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

        return 0;
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
        final String sql = "SELECT username, country, whatsapp, viber, registration_date, total_listings_created, "
                + "balance, status FROM user WHERE id = ?";
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
                    userProfileDTO.setBalance(resultSet.getInt("balance"));
                    userProfileDTO.setStatus(resultSet.getString("status"));
                }
            }
            return userProfileDTO;
        }
    }


    public void updateUserProfile(int id, UserProfileDTO updatedProfile) throws SQLException {
        final String sql = "UPDATE user SET country = ?, whatsapp = ?, viber = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // If there is no value null should be accepted
            preparedStatement.setString(1, updatedProfile.getCountry() != null && !updatedProfile.getCountry().isEmpty()
                    ? updatedProfile.getCountry()
                    : null);
            preparedStatement.setString(2, updatedProfile.getWhatsapp() != null && !updatedProfile.getWhatsapp().isEmpty()
                    ? updatedProfile.getWhatsapp()
                    : null);
            preparedStatement.setString(3, updatedProfile.getViber() != null && !updatedProfile.getViber().isEmpty()
                    ? updatedProfile.getViber()
                    : null);
            preparedStatement.setInt(4, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No user found with the provided ID");
            }
        }
    }

    public void updateUserBalanceById(int id, int balance) throws SQLException {
        final String sql = "UPDATE user SET balance = balance + ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, balance);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        }
    }

    public int getUserBalanceById(int id) throws SQLException {
        final String sql = "SELECT balance FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("balance");
            }
        }
        return 0;
    }

    public String getUserEmailById(int id) throws SQLException {
        final String sql = "SELECT email FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("email");
            }
        }
        return "";
    }

    public String getUserUsernameById(int id) throws SQLException {
        final String sql = "SELECT username FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        }
        return "";
    }

    public void updateUserStatusToVipById(int id) throws SQLException {
        final String sql = "UPDATE user SET status = 'VIP' WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }

    public String getUserStatusById(int id) throws SQLException {
        final String sql = "SELECT status FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("status");
            }
        }
        return "";
    }
}





