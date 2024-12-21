package com.example.demo.Repository;

import com.example.demo.Model.Listing.Listing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ListingRepository {

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

    public void newListing(Listing listing) throws SQLException {
        final String sql = "INSERT into listing (created_by_userid, title, description, pick_up_location, "
                + "delivery_location, must_deliver_before, weight, height, width, load_type) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, listing.getCreatedByUserid());
            preparedStatement.setString(2, listing.getTitle());
            preparedStatement.setString(3, listing.getDescription());
            preparedStatement.setString(4, listing.getPickUpLocation());
            preparedStatement.setString(5, listing.getDeliveryLocation());
            preparedStatement.setString(6, listing.getMustDeliverBefore());
            preparedStatement.setString(7, listing.getWeight());
            preparedStatement.setString(8, listing.getHeight());
            preparedStatement.setString(9, listing.getWidth());
            preparedStatement.setString(10, listing.getLoadType());

            preparedStatement.executeUpdate();
        }
    }
}
