package com.example.demo.Service;

import com.example.demo.Model.Listing.Listing;
import com.example.demo.Repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ListingService {

    @Autowired
    private ListingRepository listingRepository;

    public void newListing(Listing listing) throws SQLException {
        listingRepository.newListing(listing);
    }

    public Listing getListingById(int id) throws SQLException {
        return listingRepository.getListingById(id);
    }

    public List<Listing> getAllListingsByUserId(int id) throws SQLException {
        return listingRepository.getAllListingsByUserId(id);
    }

    public List<Listing> getAllListings() throws SQLException {
        return listingRepository.getAllListings();
    }

    public boolean deleteListingById(int id) throws SQLException {
        return listingRepository.deleteListingById(id);
    }
}
