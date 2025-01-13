package com.example.demo.Repository;

import com.example.demo.Model.Listing.Listing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        final String sql = "INSERT into listing (created_by_userid, title, description, pick_up_country, delivery_country, "
                + "pick_up_location, delivery_location, must_deliver_before, weight, height, width, load_type, image_url) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, listing.getCreatedByUserid());
            preparedStatement.setString(2, listing.getTitle());
            preparedStatement.setString(3, listing.getDescription());
            preparedStatement.setString(4, listing.getPickUpCountry());
            preparedStatement.setString(5, listing.getDeliveryCountry());
            preparedStatement.setString(6, listing.getPickUpLocation());
            preparedStatement.setString(7, listing.getDeliveryLocation());
            preparedStatement.setString(8, listing.getMustDeliverBefore());
            preparedStatement.setString(9, listing.getWeight());
            preparedStatement.setString(10, listing.getHeight());
            preparedStatement.setString(11, listing.getWidth());
            preparedStatement.setString(12, listing.getLoadType());
            preparedStatement.setString(13, listing.getBase64Image());

            preparedStatement.executeUpdate();
        }
    }

    public Listing getListingById(int id) throws SQLException {
        final String sql = "SELECT * FROM listing WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Listing listing = new Listing();

                    listing.setId(resultSet.getInt("id"));
                    listing.setCreatedByUserid(resultSet.getInt("created_by_userid"));
                    listing.setTitle(resultSet.getString("title"));
                    listing.setDescription(resultSet.getString("description"));
                    listing.setPickUpCountry(resultSet.getString("pick_up_country"));
                    listing.setDeliveryCountry(resultSet.getString("delivery_country"));
                    listing.setPickUpLocation(resultSet.getString("pick_up_location"));
                    listing.setDeliveryLocation(resultSet.getString("delivery_location"));
                    listing.setMustDeliverBefore(resultSet.getString("must_deliver_before"));
                    listing.setWeight(resultSet.getString("weight"));
                    listing.setHeight(resultSet.getString("height"));
                    listing.setWidth(resultSet.getString("width"));
                    listing.setLoadType(resultSet.getString("load_type"));
                    listing.setBase64Image(resultSet.getString("image_url"));

                    Timestamp timestamp = resultSet.getTimestamp("creation_date");
                    LocalDateTime creationDate = timestamp.toLocalDateTime();
                    String formattedDate = creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    listing.setCreatedAt(formattedDate);
                    return listing;
                }
            }
        }
        return null;
    }

    public List<Listing> getAllListingsByUserId(int id) throws SQLException {
        final String sql = "SELECT * FROM listing WHERE created_by_userid = ?";
        List<Listing> listings = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Listing listing = new Listing();

                    listing.setId(resultSet.getInt("id"));
                    listing.setCreatedByUserid(resultSet.getInt("created_by_userid"));
                    listing.setTitle(resultSet.getString("title"));
                    listing.setDescription(resultSet.getString("description"));
                    listing.setPickUpCountry(resultSet.getString("pick_up_country"));
                    listing.setDeliveryCountry(resultSet.getString("delivery_country"));
                    listing.setPickUpLocation(resultSet.getString("pick_up_location"));
                    listing.setDeliveryLocation(resultSet.getString("delivery_location"));
                    listing.setMustDeliverBefore(resultSet.getString("must_deliver_before"));
                    listing.setWeight(resultSet.getString("weight"));
                    listing.setHeight(resultSet.getString("height"));
                    listing.setWidth(resultSet.getString("width"));
                    listing.setLoadType(resultSet.getString("load_type"));
                    listing.setBase64Image(resultSet.getString("image_url"));

                    Timestamp timestamp = resultSet.getTimestamp("creation_date");
                    LocalDateTime creationDate = timestamp.toLocalDateTime();
                    String formattedDate = creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    listing.setCreatedAt(formattedDate);

                    listings.add(listing);
                }
                return listings;
            }
        }
    }

    public List<Listing> getFilteredListings(String fromCountry, String toCountry) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM listing WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (fromCountry != null && !fromCountry.isEmpty()) {
            sql.append(" AND pick_up_country = ?");
            params.add(fromCountry);
        }
        if (toCountry != null && !toCountry.isEmpty()) {
            sql.append(" AND delivery_country = ?");
            params.add(toCountry);
        }

        List<Listing> listings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Listing listing = new Listing();
                    listing.setId(resultSet.getInt("id"));
                    listing.setCreatedByUserid(resultSet.getInt("created_by_userid"));
                    listing.setTitle(resultSet.getString("title"));
                    listing.setDescription(resultSet.getString("description"));
                    listing.setPickUpCountry(resultSet.getString("pick_up_country"));
                    listing.setDeliveryCountry(resultSet.getString("delivery_country"));
                    listing.setPickUpLocation(resultSet.getString("pick_up_location"));
                    listing.setDeliveryLocation(resultSet.getString("delivery_location"));
                    listing.setMustDeliverBefore(resultSet.getString("must_deliver_before"));
                    listing.setWeight(resultSet.getString("weight"));
                    listing.setHeight(resultSet.getString("height"));
                    listing.setWidth(resultSet.getString("width"));
                    listing.setLoadType(resultSet.getString("load_type"));
                    listing.setBase64Image(resultSet.getString("image_url"));

                    Timestamp timestamp = resultSet.getTimestamp("creation_date");
                    LocalDateTime creationDate = timestamp.toLocalDateTime();
                    String formattedDate = creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    listing.setCreatedAt(formattedDate);

                    listings.add(listing);
                }
            }
        }
        return listings;
    }


    public boolean updateListing(Listing listing) throws SQLException {
        String sql = "UPDATE listing SET " +
                "title = ?, " +
                "description = ?, " +
                "pick_up_country = ?, " +
                "delivery_country = ?, " +
                "pick_up_location = ?, " +
                "delivery_location = ?, " +
                "must_deliver_before = ?, " +
                "weight = ?, " +
                "height = ?, " +
                "width = ?, " +
                "load_type = ? " +
                "WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, listing.getTitle());
            preparedStatement.setString(2, listing.getDescription());
            preparedStatement.setString(3, listing.getPickUpCountry());
            preparedStatement.setString(4, listing.getDeliveryCountry());
            preparedStatement.setString(5, listing.getPickUpLocation());
            preparedStatement.setString(6, listing.getDeliveryLocation());
            preparedStatement.setString(7, listing.getMustDeliverBefore());
            preparedStatement.setString(8, listing.getWeight());
            preparedStatement.setString(9, listing.getHeight());
            preparedStatement.setString(10, listing.getWidth());
            preparedStatement.setString(11, listing.getLoadType());
            preparedStatement.setInt(12, listing.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        }
    }


    public boolean deleteListingById(int id) throws SQLException {
        String sql = "DELETE FROM listing WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public int getTotalListingsCount() throws SQLException {
        final String sql = "SELECT COUNT(*) AS total FROM listing";
        try (Connection connection = DriverManager.getConnection(SQLURL, SQLUSERNAME, SQLPASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0;
    }

}
