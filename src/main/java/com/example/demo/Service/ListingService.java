package com.example.demo.Service;

import com.example.demo.Model.Listing.Listing;
import com.example.demo.Repository.ListingRepository;
import com.example.demo.Utility.WebSocket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ListingService {

    @Autowired
    public ListingRepository listingRepository;

    @Autowired
    private WebSocketController webSocketController;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void newListing(Listing listing) throws SQLException {
        listingRepository.newListing(listing);
    }

    public Listing getListingById(int id) throws SQLException {
        return listingRepository.getListingById(id);
    }

    public List<Listing> getAllListingsByUserId(int id) throws SQLException {
        return listingRepository.getAllListingsByUserId(id);
    }

    public List<Listing> getFilteredListings(String fromCountry, String toCountry) throws SQLException {
        return listingRepository.getFilteredListings(fromCountry, toCountry);
    }

    public boolean deleteListingById(int id) throws SQLException {
        return listingRepository.deleteListingById(id);
    }

    @Scheduled(fixedRate = 10000)
    public void updateTotalListings() {
        executorService.submit(() -> {
            try {

                int totalListings = listingRepository.getTotalListingsCount();
                webSocketController.updateTotalListings(totalListings);

            } catch (Exception e) {
                System.err.println("Error updating total listings: " + e.getMessage());
            }
        });
    }

    public boolean updateListing(Listing listing) throws SQLException {
        if (listing == null || listing.getId() <= 0) {
            throw new IllegalArgumentException("Invalid listing data provided.");
        }
        return listingRepository.updateListing(listing);
    }
}
