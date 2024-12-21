package com.example.demo.Service;

import com.example.demo.Model.Listing.Listing;
import com.example.demo.Repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ListingService {

    @Autowired
    private ListingRepository listingRepository;

    public void newListing(Listing listing) throws SQLException {
        listingRepository.newListing(listing);
    }
}
